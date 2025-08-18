package io.github.hato1883.core.modloading;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.IRegistryLoader;
import io.github.hato1883.core.registries.ModRegistrar;

import java.util.List;

public class DefaultRegistryLoader implements IRegistryLoader {

    @Override
    public void loadRegistries(List<ILoadedMod> loadedMods) {
        for (ILoadedMod mod : loadedMods) {
            try {
                mod.instance().registerModContent(new ModRegistrar());
            } catch (Exception e) {
                LogManager.getLogger(mod.id()).error("Failed to register content", e);
            }
        }
    }
}
