package io.github.hato1883.api.mod;

import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.*;

public interface IModRegistrar {
    void registerGamePhase(IGamePhase gamePhase);

    void registerBoardType(IBoardType boardType);

    void registerTileType(ITileType tileType);

    void registerResourceType(IResourceType resourceType);

    void registerBuildingType(IBuildingType buildingType);

    void registerPortType(IPortType portType);

    void registerRoadType(IRoadType roadType);
}
