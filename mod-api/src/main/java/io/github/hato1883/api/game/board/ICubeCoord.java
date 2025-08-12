package io.github.hato1883.api.game.board;


public interface ICubeCoord {

    int x();
    default int getQ() {return x();}

    int y();
    default int getR() {return y();}

    int z();
    default int getS() {return z();}

    int distanceFromOrigin();

    int distance(ICubeCoord secondCoord);

    ICubeCoord getNeighbor(int direction);

    ICubeCoord[] getNeighbors();

    ICubeCoord add(ICubeCoord other);

    ICubeCoord subtract(ICubeCoord other);
}
