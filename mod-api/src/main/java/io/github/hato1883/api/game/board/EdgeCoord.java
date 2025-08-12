package io.github.hato1883.api.game.board;

import java.util.Objects;

/**
 * Represents the coordinate of an edge in a hexagonal grid, identified by the two vertices it connects.
 * <p>
 * Provides canonical identification for edges through ordered vertex coordinates, enabling consistent
 * equality comparison and hash-based storage.
 *
 * <h3>Key Features:</h3>
 * <ul>
 *     <li>Maintains consistent vertex ordering (v1 ≤ v2)</li>
 *     <li>Pre-computes hash code for performance</li>
 *     <li>Implements value-based equality</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * VertexCoord v1 = new VertexCoord(...);
 * VertexCoord v2 = new VertexCoord(...);
 * EdgeCoord edgeCoord = new EdgeCoord(v1, v2);
 * if (edgeMap.containsKey(edgeCoord)) {
 *     Edge edge = edgeMap.get(edgeCoord);
 * }
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link VertexCoord} - The vertex coordinates this edge connects</li>
 * </ul>
 */
public class EdgeCoord {
    /** The first vertex coordinate (always ≤ v2 by hashCode) */
    private final VertexCoord v1;

    /** The second vertex coordinate (always ≥ v1 by hashCode) */
    private final VertexCoord v2;

    /** Pre-computed hash code for better performance in collections */
    private final int hashCode;

    /**
     * Creates an edge coordinate between two vertices.
     *
     * @param vertex1 first vertex coordinate
     * @param vertex2 second vertex coordinate
     * @throws IllegalArgumentException if either vertex is null
     *
     * <h3>Normalization:</h3>
     * The vertices are stored in consistent order (by hashCode) to ensure:
     * <ul>
     *     <li>EdgeCoord(v1,v2) equals EdgeCoord(v2,v1)</li>
     *     <li>Consistent hash code generation</li>
     * </ul>
     */
    public EdgeCoord(VertexCoord vertex1, VertexCoord vertex2) {
        if (vertex1 == null || vertex2 == null) {
            throw new IllegalArgumentException("Vertex coordinates cannot be null");
        }
        // Ensure consistent ordering
        if (vertex1.hashCode() < vertex2.hashCode()) {
            this.v1 = vertex1;
            this.v2 = vertex2;
        } else {
            this.v1 = vertex2;
            this.v2 = vertex1;
        }
        this.hashCode = Objects.hash(v1, v2);
    }

    /**
     * Compares this edge coordinate with another object for equality.
     *
     * @param o the object to compare with
     * @return true if the other object is an EdgeCoord with the same vertices
     *         (order insensitive)
     *
     * <h3>Equality Rules:</h3>
     * Two EdgeCoords are equal if they connect the same vertices,
     * regardless of construction order.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeCoord that)) return false;
        return v1.equals(that.v1) && v2.equals(that.v2);
    }

    /**
     * Returns the pre-computed hash code for this edge coordinate.
     *
     * @return the hash code value
     *
     * <h3>Consistency:</h3>
     * The hash code is guaranteed to be consistent with equals() because:
     * <ul>
     *     <li>It's computed from the normalized vertex order</li>
     *     <li>It's cached during construction</li>
     * </ul>
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    // TODO: Add getVertexCoords() method
    /**
     * Gets the vertex coordinates this edge connects.
     * @return array containing both vertex coordinates [v1, v2]
     *
     * <h3>Design Note:</h3>
     * Needed for:
     * <ul>
     *     <li>Edge rendering</li>
     *     <li>Pathfinding algorithms</li>
     *     <li>Network validation</li>
     * </ul>
     */
    // public VertexCoord[] getVertexCoords() { return new VertexCoord[]{v1, v2}; }
}
