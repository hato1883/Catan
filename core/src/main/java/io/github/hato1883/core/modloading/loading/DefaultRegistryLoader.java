package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.mod.load.IRegistryLoader;
import io.github.hato1883.api.services.IServiceLocator;
import io.github.hato1883.core.registries.ModRegistrar;

import java.util.List;

public class DefaultRegistryLoader implements IRegistryLoader {

    private final IServiceLocator serviceLocator;

    public DefaultRegistryLoader(IServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void loadRegistries(List<ILoadedMod> loadedMods) {
        for (ILoadedMod mod : loadedMods) {
            try {
                mod.instance().registerModContent(new ModRegistrar(serviceLocator));
            } catch (Exception e) {
                LogManager.getLogger(mod.id()).error("Failed to register content", e);
            }
        }
    }
}
