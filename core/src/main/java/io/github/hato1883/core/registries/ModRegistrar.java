package io.github.hato1883.core.registries;

import io.github.hato1883.api.registries.*;
import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.*;
import io.github.hato1883.api.mod.IModRegistrar;
import io.github.hato1883.api.services.IServiceLocator;

public class ModRegistrar implements IModRegistrar {

    private final IServiceLocator serviceLocator;

    public ModRegistrar(IServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public void registerGamePhase(IGamePhase gamePhase) {
        serviceLocator.require(IGamePhaseRegistry.class).register(gamePhase.getId(), gamePhase);
    }

    @Override
    public void registerBoardType(IBoardType boardType) {
        serviceLocator.require(IBoardTypeRegistry.class).register(boardType.getIdentifier(), boardType);
    }
    @Override
    public void registerTileType(ITileType tileType) {
        serviceLocator.require(ITileTypeRegistry.class).register(tileType.getId(), tileType);
    }
    @Override
    public void registerResourceType(IResourceType resourceType) {
        serviceLocator.require(IResourceTypeRegistry.class).register(resourceType.getId(), resourceType);
    }

    @Override
    public void registerBuildingType(IBuildingType buildingType) {
        serviceLocator.require(IBuildingTypeRegistry.class).register(buildingType.getId(), buildingType);
    }
    @Override
    public void registerPortType(IPortType portType) {
        serviceLocator.require(IPortTypeRegistry.class).register(portType.getId(), portType);
    }
    @Override
    public void registerRoadType(IRoadType roadType) {
        serviceLocator.require(IRoadTypeRegistry.class).register(roadType.getId(), roadType);
    }

    // Add more methods as needed for other content types
}
