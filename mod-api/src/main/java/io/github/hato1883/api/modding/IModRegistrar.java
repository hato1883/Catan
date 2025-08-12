package io.github.hato1883.api.modding;

import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.*;

public interface IModRegistrar {
    void registerGamePhase(IGamePhase gamePhase);

    void registerBoardType(IBoardType boardType);

    void registerTileType(ITileType tileType);

    void registerResourceType(IResourceType resourceType);

    void registerBuildingType(IBuildingType buildingType);

    void registerPortType(IPortType portType);

    void registerRoadType(IRoadType roadType);
}
