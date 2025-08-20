package io.github.hato1883.api.world.board;

public interface IBuilding extends IStructure {
    /**
     * @return the type of this building
     */
    IBuildingType getType();

    /**
     * @return the board position of this building (e.g. vertex location)
     */
    VertexCoord getPosition();
}
