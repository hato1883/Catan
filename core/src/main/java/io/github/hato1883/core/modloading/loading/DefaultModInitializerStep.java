package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.IModInitializer;
import io.github.hato1883.api.mod.load.ILoadedMod;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.util.List;

/**
 * Step 8: Initialize all loaded mods (call init methods for each entrypoint).
 */
public class DefaultModInitializerStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IModInitializer initializer;

    public DefaultModInitializerStep(IModInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public void execute(ModLoadingContext context) throws Exception {
        List<ILoadedMod> loadedMods = context.getLoadedMods();
        initializer.initializeAll(loadedMods);
        LOGGER.info("Initialization complete for {} mods.", loadedMods.size());
    }
}
