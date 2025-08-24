package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.services.IServiceLocator;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static io.github.hato1883.api.mod.load.dependency.ModDependencyException.logDependencyException;


public class ModLoader {

    private static final Logger LOGGER = LogManager.getLogger("ModLoading");

    private final List<ModLoadingStep> pipelineSteps;

    /**
     * Production factory: uses ModLoading facade for all dependencies.
     */
    public static ModLoader createDefault(IServiceLocator serviceLocator, Path modsDir, Path atlasDir, String atlasBaseName) {
        List<ModLoadingStep> steps = List.of(
            new DefaultModDiscoveryStep(serviceLocator.require(IModDiscovery.class), modsDir),
            new DefaultModMetadataStep(serviceLocator.require(IModMetadataReader.class)),
            new DefaultModDependencyResolutionStep(serviceLocator.require(IDependencyResolver.class)),
            new DefaultModInstanceCreationStep(serviceLocator.require(IModClassLoaderFactory.class)),
            new DefaultRegistryLoaderStep(serviceLocator.require(IRegistryLoader.class)),
            new DefaultModAssetLoadingStep(serviceLocator.require(IModAssetLoader.class), atlasDir, atlasBaseName),
            new DefaultEventListenerRegistrationStep(serviceLocator.require(IModListenerScanner.class)),
            new DefaultModInitializerStep(serviceLocator.require(IModInitializer.class))
        );
        return new ModLoader(steps);
    }

    /**
     * Dependency-injection constructor for testability.
     */
    public ModLoader(List<ModLoadingStep> pipelineSteps) {
        this.pipelineSteps = pipelineSteps;
    }

    /**
     * Full lifecycle: runs the modular pipeline steps in order.
     */
    public List<ILoadedMod> loadAll() throws IOException {
        ModLoadingContext context = new ModLoadingContext();
        for (ModLoadingStep step : pipelineSteps) {
            try {
                step.execute(context);
            } catch (Exception ex) {
                LOGGER.error("Mod loading failed at step {}: {}", step.getClass().getSimpleName(), ex.getMessage(), ex);
                // Optionally, add cleanup or mod removal logic here if needed
                throw new ModLoadingException("Mod loading failed at step " + step.getClass().getSimpleName(), ex);
            }
        }
        return List.copyOf(context.getLoadedMods());
    }
}
