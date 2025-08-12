package io.github.hato1883.modloader;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.hato1883.game.event.EventListenerRegistrar;
import io.github.hato1883.api.LogManager;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.github.hato1883.Main.LOGGER_ID;


/**
 * Responsible for scanning mod packages for {@code @EventListener} annotated
 * methods and registering listener instances with {@link EventListenerRegistrar}.
 *
 * <p>Design principles:
 * <ul>
 *   <li>Runs the CPU-bound classpath scanning in parallel to improve performance.</li>
 *   <li>Collects results in a thread-safe map and performs registration sequentially
 *       to avoid races in registration order.</li>
 *   <li>Has a small, well-defined public surface: {@link #scanAndRegisterAllMods(List)}</li>
 * </ul>
 *
 * <h3>Example</h3>
 * <pre>{@code
 * // From ModLoader after mods are loaded:
 * ModListenerLoader.scanAndRegisterAllMods(loadedMods);
 * }</pre>
 *
 * <h3>See also</h3>
 * <ul>
 *   <li>{@link ModLoader#loadAll()} — caller of this utility</li>
 *   <li>{@link EventListenerRegistrar} — where listener instances are registered</li>
 * </ul>
 */
public class ModListenerLoader {
    private static final Logger rootLogger = LogManager.getLogger(LOGGER_ID);

    private static final ExecutorService SCAN_POOL =
        Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() - 1));

    /**
     * Scan each supplied mod's base package in parallel for classes that contain
     * methods annotated with {@code io.github.hato1883.api.events.EventListener}.
     *
     * <p>After scanning completes, listener classes are instantiated and
     * registered via {@link EventListenerRegistrar#register(String, Object)}.
     *
     * @param mods list of mod metadata objects to scan; must not be null
     */
    public static void scanAndRegisterAllMods(List<LoadedMod> mods) {
        if (mods == null || mods.isEmpty()) {
            rootLogger.debug("No mods to scan for listeners.");
            return;
        }

        final Map<String, List<Class<?>>> modToListenerClasses = new ConcurrentHashMap<>();

        // Submit scans in parallel and coerce each Future to Future<?> to avoid capture wildcard warnings.
        List<Future<?>> futures = mods.stream()
            .map(mod -> (Future<?>) SCAN_POOL.submit(() -> scanSingleMod(mod, modToListenerClasses)))
            .collect(Collectors.toList());

        // Wait for all scans to complete and log errors via LogManager
        for (Future<?> f : futures) {
            try {
                f.get();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                rootLogger.error("Listener scan interrupted", ie);
            } catch (ExecutionException ee) {
                rootLogger.error("Listener scan task failed", ee.getCause());
            } catch (Exception e) {
                rootLogger.error("Unexpected error while waiting for listener scan to complete", e);
            }
        }

        // We do not accept new tasks for this scanning lifecycle — graceful shutdown of pool
        // (the pool can be left running if you plan to reuse; replace with managed lifecycle if desired).
        SCAN_POOL.shutdown();

        // Register found listeners sequentially to keep predictable ordering and error handling
        for (Map.Entry<String, List<Class<?>>> entry : modToListenerClasses.entrySet()) {
            final String modId = entry.getKey();
            final Logger modLogger = LogManager.getLogger(modId);

            for (Class<?> listenerClass : entry.getValue()) {
                try {
                    Object listenerInstance = listenerClass.getDeclaredConstructor().newInstance();
                    EventListenerRegistrar.register(modId, listenerInstance);
                    modLogger.debug("Registered listener instance of {}", listenerClass.getName());
                } catch (Exception e) {
                    modLogger.error("Failed to instantiate or register listener class '{}' for mod '{}'",
                        listenerClass.getName(), modId, e);
                }
            }
        }
    }

    /**
     * Scans a single mod for classes that contain {@code @EventListener} methods and
     * stores discovered classes into the provided map.
     */
    private static void scanSingleMod(LoadedMod mod, Map<String, List<Class<?>>> resultMap) {
        final Logger modLogger = LogManager.getLogger(mod.id());
        final String basePackage = getBasePackageFromEntrypoint(mod.mainClass());

        modLogger.debug("Scanning package '{}' for listeners for mod '{}'", basePackage, mod.id());

        try (ScanResult scanResult = new ClassGraph()
            .enableAllInfo()
            .acceptPackages(basePackage)
            .scan()) {

            List<Class<?>> foundClasses = new ArrayList<>();

            scanResult.getClassesWithMethodAnnotation("io.github.hato1883.api.events.EventListener")
                .forEach(classInfo -> {
                    try {
                        foundClasses.add(classInfo.loadClass());
                    } catch (Throwable t) {
                        modLogger.error("Failed to load class '{}' for mod '{}'",
                            classInfo.getName(), mod.id(), t);
                    }
                });

            if (!foundClasses.isEmpty()) {
                resultMap.put(mod.id(), foundClasses);
                modLogger.debug("Discovered {} listener classes for mod '{}'", foundClasses.size(), mod.id());
            }
        } catch (Exception e) {
            modLogger.error("Listener scan failed for mod '{}'", mod.id(), e);
        }
    }

    private static String getBasePackageFromEntrypoint(String entrypointClassName) {
        int lastDot = entrypointClassName.lastIndexOf('.');
        return (lastDot > 0) ? entrypointClassName.substring(0, lastDot) : "";
    }
}
