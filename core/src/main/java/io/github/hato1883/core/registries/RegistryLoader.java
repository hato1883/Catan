package io.github.hato1883.core.registries;

import io.github.hato1883.core.modloading.LoadedMod;
import io.github.hato1883.api.LogManager;

import java.util.List;

public final class RegistryLoader {

    /**
     * Called during mod loading phase, after all mod classes are loaded.
     * This method calls each mod’s content registration method.
     */
    public static void loadRegistries(List<LoadedMod> loadedMods) {
        for (LoadedMod mod : loadedMods) {
            try {
                mod.instance().registerModContent(new ModRegistrar());
            } catch (Exception e) {
                // Handle mod registration failures gracefully, log errors
                LogManager.getLogger(mod.id()).error("Failed to register content", e);
            }
        }

        // Optionally fire a post-registration event or validate registries here
    }
}
