package io.github.hato1883.api.world.board;

public interface IRoad extends IStructure {
    /**
     * @return the type of this road
     */
    IRoadType getType();

    /**
     * @return the board position of this road (e.g. vertex position)
     */
    EdgeCoord getPosition();
}
