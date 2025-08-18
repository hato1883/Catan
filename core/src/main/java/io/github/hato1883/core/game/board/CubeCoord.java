package io.github.hato1883.core.game.board;

import io.github.hato1883.api.game.board.ICubeCoord;

/**
 * Represents a cube coordinate in an axial hex grid system following the invariant: q + r + s == 0.
 * <p>
 * Cube coordinates are an elegant solution for hexagonal grid systems, allowing simple arithmetic
 * operations for neighbor finding, distance calculation, and pathfinding.
 *
 * <h3>Coordinate System:</h3>
 * <ul>
 *     <li>{@code q} - The q-axis component (analogous to x in cartesian)</li>
 *     <li>{@code r} - The r-axis component (analogous to z in 3D)</li>
 *     <li>{@code s} - The s-axis component (derived as -q-r to maintain invariant)</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * ICubeCoord center = new ICubeCoord(0, 0, 0);
 * ICubeCoord neighbor = center.getNeighbor(2); // Get NW neighbor
 * int dist = center.distance(new ICubeCoord(3, -2, -1));
 * }</pre>
 *
 * @param x The q-axis component (analogous to x in cartesian coordinates)
 * @param y The r-axis component (analogous to z in 3D coordinates)
 * @param z The s-axis component (derived as -q-r to maintain invariant)
 * @see <a href="https://www.redblobgames.com/grids/hexagons/">Red Blob Games: Hex Grids</a>
 */
public record CubeCoord(int x, int y, int z) implements ICubeCoord {
    /**
     * Constructs a cube coordinate ensuring the invariant x + y + z == 0.
     *
     * @param x The x-axis component → “east-west” direction
     * @param y The y-axis component → “north-east / south-west”
     * @param z The z-axis component → “north-west / south-east” (should equal -q-r)
     * @throws IllegalArgumentException if x + y + z != 0
     *
     *                                  <h3>Design Note:</h3>
     *                                  The constructor enforces the cube coordinate invariant to maintain
     *                                  mathematical consistency across all operations.
     */
    public CubeCoord {
        if (x + y + z != 0) {
            throw new IllegalArgumentException("Invalid cube coords: q + r + s must be 0");
        }
    }

    public static ICubeCoord of(int i, int i1, int i2) {
        return new CubeCoord(i, i1, i2);
    }

    /**
     * Gets the neighboring coordinate in the specified direction.
     *
     * @param direction The hexagonal direction (0-5, starting at E and proceeding clockwise)
     * @return The adjacent coordinate in the specified direction
     * @throws IllegalArgumentException if direction is outside 0-5 range
     *
     * <h3>Direction Mapping:</h3>
     * <ul>
     *     <li>0: East (q+1, r-1)</li>
     *     <li>1: Northeast (q, r-1)</li>
     *     <li>2: Northwest (q-1, r)</li>
     *     <li>3: West (q-1, r+1)</li>
     *     <li>4: Southwest (q, r+1)</li>
     *     <li>5: Southeast (q+1, r)</li>
     * </ul>
     */
    public ICubeCoord getNeighbor(int direction) {
        return switch (direction % 6) {
            case 0 -> new CubeCoord(x + 1, y - 1, z);
            case 1 -> new CubeCoord(x, y - 1, z + 1);
            case 2 -> new CubeCoord(x - 1, y, z + 1);
            case 3 -> new CubeCoord(x - 1, y + 1, z);
            case 4 -> new CubeCoord(x, y + 1, z - 1);
            case 5 -> new CubeCoord(x + 1, y, z - 1);
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    /**
     * Gets all six neighboring coordinates in clockwise order starting from East.
     *
     * @return An array of 6 adjacent coordinates
     *
     * <h3>Performance Note:</h3>
     * Creates new ICubeCoord instances for each neighbor. Consider caching results
     * if this method is called frequently.
     */
    public ICubeCoord[] getNeighbors() {
        ICubeCoord[] neighbors = new CubeCoord[6];
        for (int i = 0; i < 6; i++) {
            neighbors[i] = getNeighbor(i);
        }
        return neighbors;
    }


    public ICubeCoord add(ICubeCoord other) {
        return new CubeCoord(
            this.x() + other.x(),
            this.y() + other.y(),
            this.z + other.z()
        );
    }

    public ICubeCoord subtract(ICubeCoord other) {
        return new CubeCoord(
            this.x() - other.x(),
            this.y() - other.y(),
            this.z - other.z()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CubeCoord(int x1, int y1, int z1))) return false;
        return x == x1 && y == y1 && z == z1;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", x, y, z);
    }
}
