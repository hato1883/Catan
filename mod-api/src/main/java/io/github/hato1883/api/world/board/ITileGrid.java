package io.github.hato1883.api.world.board;

import java.util.List;

/**
 * Defines grid-specific logic for tile positions, such as neighbor calculation and vector math.
 * Implementations provide logic for different grid types (hex, square, triangle, etc).
 * Modders can implement this interface for custom grids.
 */
public interface ITileGrid {
    /**
     * Returns a list of neighboring positions for the given position.
     */
    List<ITilePosition> getNeighbors(ITilePosition pos);

    /**
     * Adds two positions according to this grid's logic.
     */
    ITilePosition add(ITilePosition a, ITilePosition b);

    /**
     * Subtracts two positions according to this grid's logic.
     */
    ITilePosition subtract(ITilePosition a, ITilePosition b);

    /**
     * Computes the dot product of two positions according to this grid's logic.
     */
    float dot(ITilePosition a, ITilePosition b);

    /**
     * Returns a list of all edges for the given tile position.
     * Each edge is represented as a pair (list of size 2) of ITilePosition.
     * The order and meaning of edges is grid-specific.
     */
    List<List<ITilePosition>> getEdges(ITilePosition pos);

    /**
     * Returns a list of all vertices for the given tile position.
     * Each vertex is represented as a set of ITilePosition (usually 2 or more).
     * The order and meaning of vertices is grid-specific.
     */
    List<List<ITilePosition>> getVertices(ITilePosition pos);

    /**
     * Returns the polygon shape (normalized vertices and origin) for rendering the given tile.
     * Vertices and origin must be in [0, 1] range.
     *
     * @param tile the tile to get the polygon shape for
     * @return the polygon shape (vertices and origin)
     */
    PolygonShape getPolygonShape(ITile tile);
}
