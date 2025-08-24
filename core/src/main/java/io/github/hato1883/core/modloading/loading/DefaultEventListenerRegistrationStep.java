package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.IModListenerScanner;
import io.github.hato1883.api.mod.load.ILoadedMod;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.util.List;

/**
 * Step 7: Register event listeners for all loaded mods.
 */
public class DefaultEventListenerRegistrationStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IModListenerScanner listenerScanner;

    public DefaultEventListenerRegistrationStep(IModListenerScanner listenerScanner) {
        this.listenerScanner = listenerScanner;
    }

    @Override
    public void execute(ModLoadingContext context) throws Exception {
        List<ILoadedMod> loadedMods = context.getLoadedMods();
        listenerScanner.scanAndRegister(loadedMods);
        LOGGER.info("Event listener registration complete for {} mods.", loadedMods.size());
    }
}
