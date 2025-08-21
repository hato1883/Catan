package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.FacadesInitializer;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;

/**
 * Module that initializes all UI-related facades after GUI services are registered.
 * Add future UI facade initializations here (e.g., assets, themes, etc).
 */
public class UIFacadeInitializationModule implements IServiceModule {
    @Override
    public void registerServices(IServiceContainer registrar) {
        FacadesInitializer.initializeScreens(registrar);
        // Future: initialize other UI-related facades here
    }

    @Override
    public String getModuleName() {
        return "UIFacadeInitializationModule";
    }
}
