package io.github.hato1883.core.game.board.elements;

import io.github.hato1883.api.game.board.IBoard;
import io.github.hato1883.api.game.board.ICubeCoord;
import io.github.hato1883.api.game.board.EdgeCoord;
import io.github.hato1883.api.game.board.VertexCoord;

import java.util.*;

/**
 * Factory class for creating and managing canonical instances of board elements (vertices and edges).
 * <p>
 * Ensures that only one instance exists for each unique vertex/edge position through coordinate-based canonicalization.
 *
 * <h3>Purpose:</h3>
 * <ul>
 *     <li>Maintains single instances of shared board elements</li>
 *     <li>Prevents duplicate objects for the same physical locations</li>
 *     <li>Provides consistent access to board elements throughout the game</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * BoardElementFactory factory = new BoardElementFactory();
 * Vertex vertex = factory.getVertex(tile1Coord, tile2Coord, tile3Coord);
 * Edge edge = factory.getEdge(vertex1Coord, vertex2Coord);
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Vertex} - The vertex objects this factory creates</li>
 *     <li>{@link Edge} - The edge objects this factory creates</li>
 *     <li>{@link IBoard} - The primary consumer of this factory</li>
 * </ul>
 */
public class BoardElementFactory {
    /** Canonical storage of all vertices by their coordinates */
    private final java.util.Map<VertexCoord, Vertex> vertexMap = new HashMap<>();

    /** Canonical storage of all edges by their coordinates */
    private final java.util.Map<EdgeCoord, Edge> edgeMap = new HashMap<>();

    /**
     * Gets or creates a vertex where three tiles meet.
     *
     * @param tile1 first adjacent tile coordinate
     * @param tile2 second adjacent tile coordinate
     * @param tile3 third adjacent tile coordinate
     * @return the canonical vertex instance for this location
     * @throws IllegalArgumentException if all three Cube coordinates are null
     *
     * <h3>Canonicalization:</h3>
     * Always returns the same vertex instance for the same three tiles,
     * creating it only if it doesn't already exist.
     *
     * <h3>Edge Cases:</h3>
     * Handles board edges where one tile coordinate may be null.
     */
    public Vertex getVertex(ICubeCoord tile1, ICubeCoord tile2, ICubeCoord tile3) {
        if (tile1 == null && tile2 == null && tile3 == null) {
            throw new IllegalArgumentException("All three coordinates cannot be null");
        }
        VertexCoord key = new VertexCoord(tile1, tile2, tile3);
        return vertexMap.computeIfAbsent(key, k -> new Vertex(key));
    }

    /**
     * Convenience method to get a vertex using an array of adjacent tiles.
     *
     * @param adjacentTiles array of exactly three adjacent tile coordinates
     * @return the canonical vertex instance
     * @throws IllegalArgumentException if array is null or doesn't contain exactly 3 elements
     *
     * <h3>Implementation Note:</h3>
     * Delegates to {@link #getVertex(ICubeCoord, ICubeCoord, ICubeCoord)} after array validation.
     */
    private Vertex getVertex(ICubeCoord[] adjacentTiles) {
        if (adjacentTiles == null || adjacentTiles.length != 3) {
            throw new IllegalArgumentException("Must provide exactly 3 adjacent tiles");
        }
        return getVertex(adjacentTiles[0], adjacentTiles[1], adjacentTiles[2]);
    }

    /**
     * Convenience method to get a vertex using an array of adjacent tiles.
     *
     * @param adjacentTiles array of exactly three adjacent tile coordinates
     * @return the canonical vertex instance
     * @throws IllegalArgumentException if array is null or doesn't contain exactly 3 elements
     *
     * <h3>Implementation Note:</h3>
     * Delegates to {@link #getVertex(ICubeCoord, ICubeCoord, ICubeCoord)} after array validation.
     */
    private Vertex getVertex(List<ICubeCoord> adjacentTiles) {
        if (adjacentTiles == null || adjacentTiles.size() != 3) {
            throw new IllegalArgumentException("Must provide exactly 3 adjacent tiles");
        }
        return getVertex(adjacentTiles.get(0), adjacentTiles.get(1), adjacentTiles.get(2));
    }

    /**
     * Gets or creates an edge connecting two vertices.
     *
     * @param v1 coordinates of first vertex
     * @param v2 coordinates of second vertex
     * @return the canonical edge instance for this connection
     * @throws IllegalArgumentException if either vertex coordinate is null
     *
     * <h3>Canonicalization:</h3>
     * <ul>
     *     <li>Order of vertices doesn't matter (v1,v2 == v2,v1)</li>
     *     <li>Always returns the same edge instance for the same vertex pair</li>
     * </ul>
     *
     * <h3>Side Effects:</h3>
     * <ul>
     *     <li>Ensures both endpoint vertices exist in the factory</li>
     *     <li>The created edge automatically registers itself with both vertices</li>
     * </ul>
     */
    public Edge getEdge(VertexCoord v1, VertexCoord v2) {
        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException("Vertex coordinates cannot be null");
        }
        EdgeCoord key = new EdgeCoord(v1, v2);
        return edgeMap.computeIfAbsent(key, k -> {
            Vertex vertex1 = getVertex(v1.getAdjacentTiles());
            Vertex vertex2 = getVertex(v2.getAdjacentTiles());
            return new Edge(vertex1, vertex2);  // Edge constructor now handles vertex registration
        });
    }


    public Collection<Vertex> getAllVertices() {
        return Collections.unmodifiableCollection(vertexMap.values());
    }

    public Collection<Edge> getAllEdges() {
        return Collections.unmodifiableCollection(edgeMap.values());
    }
}
