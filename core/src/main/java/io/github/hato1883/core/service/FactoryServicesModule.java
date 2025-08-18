package io.github.hato1883.core.service;

import io.github.hato1883.api.factories.IResourceTypeFactory;
import io.github.hato1883.api.factories.ITileTypeFactory;
import io.github.hato1883.api.service.IServiceModule;
import io.github.hato1883.api.service.IServiceRegistrar;
import io.github.hato1883.core.game.board.TileTypeFactoryImpl;
import io.github.hato1883.core.game.resource.ResourceTypeFactoryImpl;

import java.util.function.Supplier;

public class FactoryServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceRegistrar registrar) {
        registrar.register(ITileTypeFactory.class, (Supplier<? extends ITileTypeFactory>) TileTypeFactoryImpl::new);
        registrar.register(IResourceTypeFactory.class, (Supplier<? extends IResourceTypeFactory>) ResourceTypeFactoryImpl::new);
        // Add more factories as needed
    }

    @Override
    public String getModuleName() {
        return "FactoryServices";
    }
}
