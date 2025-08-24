package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.IRegistryLoader;
import io.github.hato1883.api.mod.load.ILoadedMod;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.util.List;

/**
 * Step 6: Register mod content (custom types, events, etc.).
 */
public class DefaultRegistryLoaderStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IRegistryLoader registryLoader;

    public DefaultRegistryLoaderStep(IRegistryLoader registryLoader) {
        this.registryLoader = registryLoader;
    }

    @Override
    public void execute(ModLoadingContext context) throws Exception {
        List<ILoadedMod> loadedMods = context.getLoadedMods();
        registryLoader.loadRegistries(loadedMods);
        LOGGER.info("Registry loading complete for {} mods.", loadedMods.size());
    }
}
