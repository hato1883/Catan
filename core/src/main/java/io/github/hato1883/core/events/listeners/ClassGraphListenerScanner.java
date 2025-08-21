package io.github.hato1883.core.events.listeners;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.ModLoading;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.IModListenerScanner;
import io.github.hato1883.api.async.IAsyncExecutionService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Uses ClassGraph to find classes with methods annotated with EventListener.
 * Executor is injected to allow lifecycle control and easier testing.
 */
public class ClassGraphListenerScanner implements IModListenerScanner {

    private static final Logger LOGGER = LogManager.getLogger("ListenerScanner");

    private final IAsyncExecutionService executor;

    public ClassGraphListenerScanner(IAsyncExecutionService executor) {
        this.executor = executor;
    }

    @Override
    public void scanAndRegister(List<ILoadedMod> mods) {
        if (mods == null || mods.isEmpty()) return;

        List<Future<List<Class<?>>>> futures = new ArrayList<>();
        for (ILoadedMod mod : mods) {
            futures.add(executor.executeAsync(() -> scanMod(mod), "file-scan"));
        }

        // gather results sequentially and register (to preserve predictable ordering)
        for (int i = 0; i < mods.size(); i++) {
            ILoadedMod mod = mods.get(i);
            try {
                List<Class<?>> classes = futures.get(i).get();
                for (Class<?> c : classes) {
                    try {
                        Object inst = c.getDeclaredConstructor().newInstance();
                        ModLoading.listenerRegistrar().register(mod.id(), inst);
                    } catch (Throwable t) {
                        LogManager.getLogger(mod.id()).error("Failed to instantiate listener {}", c.getName(), t);
                    }
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                LOGGER.error("Listener scan interrupted", ie);
            } catch (ExecutionException ee) {
                LOGGER.error("Listener scan failed for mod {}", mod.id(), ee.getCause());
            }
        }
    }

    private List<Class<?>> scanMod(ILoadedMod mod) {
        String base = basePackageOf(mod.mainClass());
        try (ScanResult sr = new ClassGraph()
            .enableAllInfo()
            .acceptPackages(base)
            .overrideClassLoaders(mod.classLoader())
            .scan()) {

            return new ArrayList<>(sr.getClassesWithMethodAnnotation("io.github.hato1883.api.events.EventListener")
                .loadClasses());

        } catch (Throwable t) {
            LogManager.getLogger(mod.id()).error("Error scanning mod for listeners", t);
            return Collections.emptyList();
        }
    }

    private static String basePackageOf(String entrypoint) {
        int idx = entrypoint.lastIndexOf('.');
        return idx > 0 ? entrypoint.substring(0, idx) : "";
    }
}
