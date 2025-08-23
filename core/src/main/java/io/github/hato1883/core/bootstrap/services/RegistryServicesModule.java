package io.github.hato1883.core.bootstrap.services;

import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.factories.ITileTypeFactory;
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

    private void registerGameRegistries(IServiceContainer registrar) {
        registrar.registerIfAbsent(
            IBoardTypeRegistry.class, (Supplier<? extends IBoardTypeRegistry>) () -> new BoardTypeRegistry(
                registrar.require(IEventBusService.class)
            )
        );
        registrar.registerIfAbsent(
            IBuildingTypeRegistry.class, (Supplier<? extends IBuildingTypeRegistry>) () -> new BuildingTypeRegistry(
                registrar.require(IEventBusService.class)
            )
        );
        registrar.registerIfAbsent(
            IGamePhaseRegistry.class, (Supplier<? extends IGamePhaseRegistry>) () -> new GamePhaseRegistry(
                registrar.require(IEventBusService.class)
            )
        );
        registrar.registerIfAbsent(
            IPortTypeRegistry.class, (Supplier<? extends IPortTypeRegistry>) () -> new PortTypeRegistry(
                registrar.require(IEventBusService.class)
            )
        );
    }

    private void registerUIRegistries(IServiceContainer registrar) {
        registrar.registerIfAbsent(
            IUIBatchingJobRegistry.class, (Supplier<? extends IUIBatchingJobRegistry>) () -> new UIBatchingJobRegistry(
                registrar.require(IEventBusService.class)
            )
        );
        // Add other UI-related registries here as needed
    }

    private void registerTypeRegistries(IServiceContainer registrar) {
        registrar.registerIfAbsent(
            IResourceTypeRegistry.class, (Supplier<? extends IResourceTypeRegistry>) () -> new ResourceTypeRegistry(
                registrar.require(IEventBusService.class)
            )
        );
        registrar.registerIfAbsent(
            IRoadTypeRegistry.class, (Supplier<? extends IRoadTypeRegistry>) () -> new RoadTypeRegistry(
                registrar.require(IEventBusService.class)
            )
        );
        registrar.registerIfAbsent(
            ITileTypeRegistry.class, (Supplier<? extends ITileTypeRegistry>) () -> new TileTypeRegistry(
                registrar.require(IEventBusService.class),
                registrar.require(ITileTypeFactory.class)
            )
        );
    }
}
