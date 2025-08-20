package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.mod.load.IModInitializer;
import io.github.hato1883.api.mod.load.ILoadedMod;

import java.util.List;

public class DefaultModInitializer implements IModInitializer {

    @Override
    public void initializeAll(List<ILoadedMod> mods) {
        for (ILoadedMod m : mods) {
            try {
                LogManager.getLogger(m.id()).info("Initializing mod {}", m.id());
                ClassLoader prev = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(m.classLoader());
                    m.instance().onInitialize();
                } finally {
                    Thread.currentThread().setContextClassLoader(prev);
                }
            } catch (Throwable t) {
                LogManager.getLogger(m.id()).error("Error initializing mod", t);
            }
        }
    }
}
