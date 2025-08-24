package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.mod.load.dependency.ModWithPath;
import io.github.hato1883.api.mod.load.ModMetadata;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.util.List;
import java.util.Map;

/**
 * Step 3: Resolve dependencies and determine mod load order.
 */
public class DefaultModDependencyResolutionStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IDependencyResolver dependencyResolver;

    public DefaultModDependencyResolutionStep(IDependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public void execute(ModLoadingContext context) throws Exception {
        Map<ModMetadata, java.nio.file.Path> metadataMap = context.getModMetadataMap();
        LOGGER.info("Resolving mod dependencies and load order...");
        List<ModWithPath> orderedMods = dependencyResolver.resolveLoadOrder(metadataMap);
        context.setOrderedMods(orderedMods);
        LOGGER.info("Dependency resolution complete. {} mods ordered.", orderedMods.size());
    }
}

