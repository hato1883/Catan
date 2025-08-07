package io.github.hato1883.game.board.elements.vertex;

import io.github.hato1883.game.board.CubeCoord;
import io.github.hato1883.game.board.elements.Vertex;

import java.util.*;

/**
 * Represents the coordinate of a vertex in a hexagonal grid, identified by the three adjacent tiles that meet at this point.
 * <p>
 * Provides canonical identification for vertices through sorted tile coordinates, enabling consistent equality comparison
 * and hash-based storage.
 *
 * <h3>Invariants:</h3>
 * <ul>
 *     <li>Always contains exactly 3 tile coordinates (some may be null at board edges)</li>
 *     <li>Coordinates are stored in consistent sorted order</li>
 *     <li>Hash code is pre-computed for performance</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * VertexCoord vertexCoord = new VertexCoord(tile1, tile2, tile3);
 * if (vertexMap.containsKey(vertexCoord)) {
 *     Vertex vertex = vertexMap.get(vertexCoord);
 * }
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link CubeCoord} - The coordinate system used for adjacent tiles</li>
 *     <li>{@link Vertex} - The game element identified by these coordinates</li>
 * </ul>
 */
public class VertexCoord {
    /**
     * The three adjacent tiles that define this vertex's position.
     * <p>
     * Stored in consistent sorted order to ensure equality comparison works correctly.
     * May contain null values for vertices at the edge of the board.
     */
    private final CubeCoord[] adjacentTiles; // Always exactly 3 tiles

    /**
     * Pre-computed hash code for better performance in hash-based collections.
     */
    private final int hashCode;

    /**
     * Creates a vertex coordinate from three adjacent tile coordinates.
     *
     * @param tile1 first adjacent tile coordinate (may be null for edge vertices)
     * @param tile2 second adjacent tile coordinate (may be null for edge vertices)
     * @param tile3 third adjacent tile coordinate (may be null for edge vertices)
     * @throws IllegalArgumentException if all three Cube coordinates are null
     *
     * <h3>Sorting Behavior:</h3>
     * The input coordinates are sorted by their hash codes to ensure:
     * <ul>
     *     <li>Consistent equality comparison regardless of input order</li>
     *     <li>Canonical representation for hash-based storage</li>
     * </ul>
     *
     * <h3>Edge Cases:</h3>
     * At least two coordinates should be non-null for valid board positions.
     */
    public VertexCoord(CubeCoord tile1, CubeCoord tile2, CubeCoord tile3) {
        if (tile1 == null && tile2 == null && tile3 == null) {
            throw new IllegalArgumentException("All three coordinates cannot be null");
        }
        // Sort the tiles for consistent equality comparison
        this.adjacentTiles = new CubeCoord[]{tile1, tile2, tile3};
        Arrays.sort(this.adjacentTiles, Comparator.comparingInt(CubeCoord::hashCode));

        // Pre-compute hash for performance
        this.hashCode = Objects.hash(adjacentTiles[0], adjacentTiles[1], adjacentTiles[2]);
    }

    /**
     * Compares this vertex coordinate with another object for equality.
     *
     * @param o the object to compare with
     * @return true if the other object is a VertexCoord with identical adjacent tiles
     *
     * <h3>Comparison Rules:</h3>
     * Two VertexCoords are equal if their sorted adjacent tile arrays are equal,
     * with null values treated as equal to other null values.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VertexCoord that)) return false;
        return Arrays.equals(adjacentTiles, that.adjacentTiles);
    }

    /**
     * Returns the pre-computed hash code for this vertex coordinate.
     *
     * @return the hash code value
     *
     * <h3>Consistency:</h3>
     * The hash code is guaranteed to be consistent with equals() because:
     * <ul>
     *     <li>It's computed from the same sorted tile array used for equality</li>
     *     <li>It's cached during construction</li>
     * </ul>
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * Gets the list of adjacent tile coordinates that define this vertex's position.
     *
     * @return list of exactly three adjacent tile coordinates (some may be null)
     *
     * <h3>Note:</h3>
     * The returned list is sorted by tile hash codes and should not be modified.
     */
    public List<CubeCoord> getAdjacentTiles() {
        return List.of(adjacentTiles);
    }
}
