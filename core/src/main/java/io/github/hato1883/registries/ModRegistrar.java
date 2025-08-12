package io.github.hato1883.registries;

import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.*;
import io.github.hato1883.api.modding.IModRegistrar;

public class ModRegistrar implements IModRegistrar {

    @Override
    public void registerGamePhase(IGamePhase gamePhase) {
        RegistryManager.getGamePhaseRegistry().register(gamePhase.getId(), gamePhase);
    }

    @Override
    public void registerBoardType(IBoardType boardType) {
        RegistryManager.getBoardTypeRegistry().register(boardType.getId(), boardType);
    }
    @Override
    public void registerTileType(ITileType tileType) {
        RegistryManager.getTileTypeRegistry().register(tileType.getId(), tileType);
    }
    @Override
    public void registerResourceType(IResourceType resourceType) {
        RegistryManager.getResourceTypeRegistry().register(resourceType.getId(), resourceType);
    }

    @Override
    public void registerBuildingType(IBuildingType buildingType) {
        RegistryManager.getBuildingTypeRegistry().register(buildingType.getId(), buildingType);
    }
    @Override
    public void registerPortType(IPortType portType) {
        RegistryManager.getPortTypeRegistry().register(portType.getId(), portType);
    }
    @Override
    public void registerRoadType(IRoadType roadType) {
        RegistryManager.getRoadTypeRegistry().register(roadType.getId(), roadType);
    }

    // Add more methods as needed for other content types
}

