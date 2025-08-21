package io.github.hato1883.api.world.board;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IStructurableBoard extends IBoard {
    Collection<IStructure> getStructures();
    Collection<IBuilding> getBuildings();
    Collection<IRoad> getRoads();
    Collection<IPort> getPorts();

    Map<IBuildingType, java.util.List<IBuilding>> getBuildingsGroupedByType();
    Map<IRoadType, java.util.List<IRoad>> getRoadsGroupedByType();
    Map<IPortType, java.util.List<IPort>> getPortsGroupedByType();
}

