package io.github.hato1883.api.game.board;


public interface ICubeCoord {

    int x();
    default int getQ() {return x();}

    int y();
    default int getR() {return y();}

    int z();
    default int getS() {return z();}

    /**
     * Calculates the distance from the origin (0, 0, 0) using the formula:
     * (|q| + |r| + |s|) / 2.
     *
     * @return The number of hexes between this coordinate and the origin
     *
     * <h3>Mathematical Basis:</h3>
     * The distance formula works because in cube coordinates, each step must
     * change two coordinates to maintain the q + r + s = 0 invariant.
     */
    default int distanceFromOrigin() {
        return (Math.abs(x()) + Math.abs(y()) + Math.abs(z())) / 2;
    }

    /**
     * Calculates the distance between this coordinate and another coordinate.
     *
     * @param secondCoord The target coordinate to measure distance to
     * @return The number of hexes between the two coordinates
     *
     * <h3>Example:</h3>
     * <pre>{@code
     * ICubeCoord a = new ICubeCoord(3, -2, -1);
     * ICubeCoord b = new ICubeCoord(1, -1, 0);
     * int dist = a.distance(b); // Returns 2
     * }</pre>
     */
    default int distance(ICubeCoord secondCoord) {
        return (Math.abs(x() - secondCoord.x()) + Math.abs(y() - secondCoord.y()) + Math.abs(z() - secondCoord.z())) / 2;
    }

    ICubeCoord getNeighbor(int direction);

    ICubeCoord[] getNeighbors();

    ICubeCoord add(ICubeCoord other);

    ICubeCoord subtract(ICubeCoord other);

    default int dot(ICubeCoord other) {
        return x() * other.x() + y() * other.y() + z() * other.z();
    }
}
