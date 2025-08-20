package io.github.hato1883.core.registries;

import io.github.hato1883.api.Registries;
import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.*;
import io.github.hato1883.api.mod.IModRegistrar;

public class ModRegistrar implements IModRegistrar {

    @Override
    public void registerGamePhase(IGamePhase gamePhase) {
        Registries.phases().register(gamePhase.getId(), gamePhase);
    }

    @Override
    public void registerBoardType(IBoardType boardType) {
        Registries.boards().register(boardType.getIdentifier(), boardType);
    }
    @Override
    public void registerTileType(ITileType tileType) {
        Registries.tiles().register(tileType.getId(), tileType);
    }
    @Override
    public void registerResourceType(IResourceType resourceType) {
        Registries.resources().register(resourceType.getId(), resourceType);
    }

    @Override
    public void registerBuildingType(IBuildingType buildingType) {
        Registries.buildings().register(buildingType.getId(), buildingType);
    }
    @Override
    public void registerPortType(IPortType portType) {
        Registries.ports().register(portType.getId(), portType);
    }
    @Override
    public void registerRoadType(IRoadType roadType) {
        Registries.roads().register(roadType.getId(), roadType);
    }

    // Add more methods as needed for other content types
}

