package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.dependency.ModWithPath;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.nio.file.Path;
import java.util.*;
import io.github.hato1883.api.mod.load.IModClassLoaderFactory;
import io.github.hato1883.api.mod.load.ILoadedMod;

/**
 * Step 4: Instantiate mod classes and store loaded mod instances in the context.
 */
public class DefaultModInstanceCreationStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IModClassLoaderFactory classLoaderFactory;

    public DefaultModInstanceCreationStep(IModClassLoaderFactory classLoaderFactory) {
        this.classLoaderFactory = classLoaderFactory;
    }

    @Override
    public void execute(ModLoadingContext context) {
        List<ModWithPath> mods = context.getOrderedMods();
        List<ILoadedMod> loaded = new ArrayList<>(mods.size());
        Set<String> failedIds = new HashSet<>();
        for (ModWithPath mod : mods) {
            ModMetadata meta = mod.metadata();
            Path modPath = mod.path();
            boolean hasFailedDep = false;
            for (var dep : meta.dependencies()) {
                if (!dep.optional() && failedIds.contains(dep.modId())) {
                    hasFailedDep = true;
                    break;
                }
            }
            if (hasFailedDep) {
                failedIds.add(meta.id());
                LOGGER.warn("Skipping mod '{}' due to failed dependency.", meta.id());
                continue;
            }
            try {
                ClassLoader cl = classLoaderFactory.createClassLoader(modPath);
                CatanMod instance = instantiateMod(meta.entrypoint(), cl);
                loaded.add(new LoadedMod(modPath, meta, instance, cl));
                LOGGER.info("Loaded mod {} v{}", meta.id(), meta.version());
            } catch (Exception e) {
                LOGGER.error("Failed to load mod '{}' from {}: {}", meta.id(), modPath.getFileName(), e.getMessage(), e);
                failedIds.add(meta.id());
            }
        }
        loaded.removeIf(m -> failedIds.contains(m.metadata().id()));
        context.setLoadedMods(loaded);
    }

    private CatanMod instantiateMod(String mainClass, ClassLoader cl) throws ReflectiveOperationException {
        Class<?> clazz = Class.forName(mainClass, true, cl);
        Object inst = clazz.getDeclaredConstructor().newInstance();
        if (!(inst instanceof CatanMod)) {
            throw new IllegalStateException("Entrypoint class does not implement CatanMod: " + mainClass);
        }
        return (CatanMod) inst;
    }
}
