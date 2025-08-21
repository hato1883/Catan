package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.FacadesInitializer;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;

public class FactoriesFacadeModule implements IServiceModule {
    @Override
    public void registerServices(IServiceContainer registrar) {
        FacadesInitializer.initializeFactories(registrar);
    }
    @Override
    public String getModuleName() {
        return "FactoriesFacadeModule";
    }
}

