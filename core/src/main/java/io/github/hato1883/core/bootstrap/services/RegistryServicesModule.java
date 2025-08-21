package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.registries.*;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.services.IServiceModule;
import io.github.hato1883.api.services.IServiceRegistrar;
import io.github.hato1883.core.registries.*;

import java.util.function.Supplier;

public class RegistryServicesModule implements IServiceModule {

    @Override
    public void registerServices(IServiceContainer registrar) {
        registerGameRegistries(registrar);
        registerUIRegistries(registrar);
        registerTypeRegistries(registrar);
    }

    @Override
    public String getModuleName() {
        return "RegistryServices";
    }

    private void registerGameRegistries(IServiceRegistrar registrar) {
        registrar.registerIfAbsent(IBoardTypeRegistry.class, (Supplier<? extends IBoardTypeRegistry>) BoardTypeRegistry::new);
        registrar.registerIfAbsent(IBuildingTypeRegistry.class, (Supplier<? extends IBuildingTypeRegistry>) BuildingTypeRegistry::new);
        registrar.registerIfAbsent(IGamePhaseRegistry.class, (Supplier<? extends IGamePhaseRegistry>) GamePhaseRegistry::new);
        registrar.registerIfAbsent(IPortTypeRegistry.class, (Supplier<? extends IPortTypeRegistry>) PortTypeRegistry::new);
    }

    private void registerUIRegistries(IServiceRegistrar registrar) {
        registrar.registerIfAbsent(IUIBatchingJobRegistry.class, (Supplier<? extends IUIBatchingJobRegistry>) UIBatchingJobRegistry::new);
        // Add other UI-related registries here as needed
    }

    private void registerTypeRegistries(IServiceRegistrar registrar) {
        registrar.registerIfAbsent(IResourceTypeRegistry.class, (Supplier<? extends IResourceTypeRegistry>) ResourceTypeRegistry::new);
        registrar.registerIfAbsent(IRoadTypeRegistry.class, (Supplier<? extends IRoadTypeRegistry>) RoadTypeRegistry::new);
        registrar.registerIfAbsent(ITileTypeRegistry.class, (Supplier<? extends ITileTypeRegistry>) TileTypeRegistry::new);
    }
}
