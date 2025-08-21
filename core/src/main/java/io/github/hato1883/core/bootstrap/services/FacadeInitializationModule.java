package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.FacadesInitializer;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;

/**
 * Module that initializes all core facades after all services are registered.
 * This should be added last to the module list to ensure all dependencies are available.
 */
public class FacadeInitializationModule implements IServiceModule {
    @Override
    public void registerServices(IServiceContainer registrar) {
        FacadesInitializer.initializeAll(registrar);
    }

    @Override
    public String getModuleName() {
        return "FacadeInitializationModule";
    }
}

