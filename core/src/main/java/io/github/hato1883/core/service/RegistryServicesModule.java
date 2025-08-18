package io.github.hato1883.core.service;

import io.github.hato1883.api.registries.*;
import io.github.hato1883.api.service.IServiceModule;
import io.github.hato1883.api.service.IServiceRegistrar;
import io.github.hato1883.core.registries.*;

import java.util.function.Supplier;

public class RegistryServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceRegistrar registrar) {
        registerGameRegistries(registrar);
        registerTypeRegistries(registrar);
    }

    @Override
    public String getModuleName() {
        return "RegistryServices";
    }

    private void registerGameRegistries(IServiceRegistrar registrar) {
        registrar.register(IBoardTypeRegistry.class, (Supplier<? extends IBoardTypeRegistry>) BoardTypeRegistry::new);
        registrar.register(IBuildingTypeRegistry.class, (Supplier<? extends IBuildingTypeRegistry>) BuildingTypeRegistry::new);
        registrar.register(IGamePhaseRegistry.class, (Supplier<? extends IGamePhaseRegistry>) GamePhaseRegistry::new);
        registrar.register(IPortTypeRegistry.class, (Supplier<? extends IPortTypeRegistry>) PortTypeRegistry::new);
    }

    private void registerTypeRegistries(IServiceRegistrar registrar) {
        registrar.register(IResourceTypeRegistry.class, (Supplier<? extends IResourceTypeRegistry>) ResourceTypeRegistry::new);
        registrar.register(IRoadTypeRegistry.class, (Supplier<? extends IRoadTypeRegistry>) RoadTypeRegistry::new);
        registrar.register(ITileTypeRegistry.class, (Supplier<? extends ITileTypeRegistry>) TileTypeRegistryImpl::new);
    }
}

