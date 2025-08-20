package io.github.hato1883.api.world.board;

import java.util.List;

public interface IPort extends IStructure {
    IPortType getType();

    /**
     * @return the board position of this port (e.g. vertex position)
     */
    ICubeCoord getPosition();

    /**
     * @return buildings connected to this port
     */
    List<IBuilding> getConnectedBuildings();

    /**
     * @return roads connected to this port (optional, useful for mods)
     */
    List<IRoad> getConnectedRoads();
}
