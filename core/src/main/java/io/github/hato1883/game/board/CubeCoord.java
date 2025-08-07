package io.github.hato1883.game.board;

import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

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
 * CubeCoord center = new CubeCoord(0, 0, 0);
 * CubeCoord neighbor = center.getNeighbor(2); // Get NW neighbor
 * int dist = center.distance(new CubeCoord(3, -2, -1));
 * }</pre>
 *
 * @see <a href="https://www.redblobgames.com/grids/hexagons/">Red Blob Games: Hex Grids</a>
 */
public class CubeCoord {
    /** The q-axis component (analogous to x in cartesian coordinates) */
    public final int q;

    /** The r-axis component (analogous to z in 3D coordinates) */
    public final int r;

    /** The s-axis component (derived as -q-r to maintain invariant) */
    public final int s;

    /**
     * Constructs a cube coordinate ensuring the invariant q + r + s == 0.
     *
     * @param q The q-axis component
     * @param r The r-axis component
     * @param s The s-axis component (should equal -q-r)
     * @throws IllegalArgumentException if q + r + s != 0
     *
     * <h3>Design Note:</h3>
     * The constructor enforces the cube coordinate invariant to maintain
     * mathematical consistency across all operations.
     */
    public CubeCoord(int q, int r, int s) {
        if (q + r + s != 0) {
            throw new IllegalArgumentException("Invalid cube coords: q + r + s must be 0");
        }
        this.q = q;
        this.r = r;
        this.s = s;
    }

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
    public int distance() {
        return (Math.abs(this.q) + Math.abs(this.r) + Math.abs(s)) / 2;
    }

    /**
     * Calculates the distance between this coordinate and another coordinate.
     *
     * @param secondCoord The target coordinate to measure distance to
     * @return The number of hexes between the two coordinates
     *
     * <h3>Example:</h3>
     * <pre>{@code
     * CubeCoord a = new CubeCoord(3, -2, -1);
     * CubeCoord b = new CubeCoord(1, -1, 0);
     * int dist = a.distance(b); // Returns 2
     * }</pre>
     */
    public int distance(CubeCoord secondCoord) {
        return (Math.abs(this.q - secondCoord.q) + Math.abs(this.r - secondCoord.r) + Math.abs(s - secondCoord.s)) / 2;
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
    public CubeCoord getNeighbor(int direction) {
        return switch (direction % 6) {
            case 0 -> new CubeCoord(q+1, r-1, s);
            case 1 -> new CubeCoord(q, r-1, s+1);
            case 2 -> new CubeCoord(q-1, r, s+1);
            case 3 -> new CubeCoord(q-1, r+1, s);
            case 4 -> new CubeCoord(q, r+1, s-1);
            case 5 -> new CubeCoord(q+1, r, s-1);
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    /**
     * Gets all six neighboring coordinates in clockwise order starting from East.
     *
     * @return An array of 6 adjacent coordinates
     *
     * <h3>Performance Note:</h3>
     * Creates new CubeCoord instances for each neighbor. Consider caching results
     * if this method is called frequently.
     */
    public CubeCoord[] getAllNeighbors() {
        CubeCoord[] neighbors = new CubeCoord[6];
        for (int i = 0; i < 6; i++) {
            neighbors[i] = getNeighbor(i);
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CubeCoord that)) return false;
        return q == that.q && r == that.r && s == that.s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r, s);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", q, r, s);
    }

    public static Vector2 cubeToWorld(CubeCoord coord, float radius, float gap) {
        float width = (float) (Math.sqrt(3) * radius);
        float height = 2f * radius;

        float xSpacing = width + gap;
        float ySpacing = height * 0.75f + gap;

        float x = xSpacing * (coord.q + coord.r / 2f);
        float y = ySpacing * -coord.r;

        return new Vector2(x, y);
    }
}
