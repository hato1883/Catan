package io.github.hato1883.api.world.board;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBoard {

    void setName(String name);
    void setDimensions(Dimension dimension);

    void addTile(IHexTile tile);
    Collection<IHexTile> getTiles();
    Map<ITileType, List<IHexTile>> getTilesGroupedByTileType();
    Optional<IHexTile> getTile(ICubeCoord coord);
    int getTileCount();

    Collection<IStructure> getStructures();
    Collection<IBuilding> getBuildings();
    Collection<IRoad> getRoads();
    Collection<IPort> getPorts();

    Map<IBuildingType, List<IBuilding>> getBuildingsGroupedByType();
    Map<IRoadType, List<IRoad>> getRoadsGroupedByType();
    Map<IPortType, List<IPort>> getPortsGroupedByType();

    Collection<IHexTile> getNeighbors(IHexTile tile);

    String getName();
    Dimension getDimensions();

    /** Trigger resource production for a given dice roll */
    void triggerProductionForRoll(int rolledNumber);
}
