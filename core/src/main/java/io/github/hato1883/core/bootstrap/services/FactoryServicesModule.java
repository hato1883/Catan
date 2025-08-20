package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.factories.IResourceTypeFactory;
import io.github.hato1883.api.factories.ITileTypeFactory;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;
import io.github.hato1883.api.services.IServiceRegistrar;
import io.github.hato1883.core.factories.TileTypeFactory;
import io.github.hato1883.core.factories.ResourceTypeFactory;

import java.util.function.Supplier;

public class FactoryServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceContainer registrar) {
        registrar.registerIfAbsent(ITileTypeFactory.class, (Supplier<? extends ITileTypeFactory>) TileTypeFactory::new);
        registrar.registerIfAbsent(IResourceTypeFactory.class, (Supplier<? extends IResourceTypeFactory>) ResourceTypeFactory::new);
        // Add more factories as needed
    }

    @Override
    public String getModuleName() {
        return "FactoryServices";
    }
}
