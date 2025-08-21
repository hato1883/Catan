package io.github.hato1883.api.world.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of a standard axis-aligned square grid.
 * - Neighbors are the four cardinal directions (no diagonals).
 * - Edges and vertices are defined for canonicalization and placement logic.
 */
public class SquareGrid implements ISquareGrid {
    @Override
    public List<ITilePosition> getNeighbors(ITilePosition pos) {
        // Cardinal directions: +x, -x, +y, -y (z is ignored for 2D grid)
        List<ITilePosition> neighbors = new ArrayList<>(4);
        neighbors.add(new TilePosition(pos.x() + 1, pos.y(), pos.z()));
        neighbors.add(new TilePosition(pos.x() - 1, pos.y(), pos.z()));
        neighbors.add(new TilePosition(pos.x(), pos.y() + 1, pos.z()));
        neighbors.add(new TilePosition(pos.x(), pos.y() - 1, pos.z()));
        return neighbors;
    }

    @Override
    public ITilePosition add(ITilePosition a, ITilePosition b) {
        return new TilePosition(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
    }

    @Override
    public ITilePosition subtract(ITilePosition a, ITilePosition b) {
        return new TilePosition(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
    }

    @Override
    public float dot(ITilePosition a, ITilePosition b) {
        return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
    }

    @Override
    public List<List<ITilePosition>> getEdges(ITilePosition pos) {
        // Each edge is a pair of positions (this tile and its neighbor)
        List<List<ITilePosition>> edges = new ArrayList<>(4);
        edges.add(Arrays.asList(pos, new TilePosition(pos.x() + 1, pos.y(), pos.z()))); // East
        edges.add(Arrays.asList(pos, new TilePosition(pos.x() - 1, pos.y(), pos.z()))); // West
        edges.add(Arrays.asList(pos, new TilePosition(pos.x(), pos.y() + 1, pos.z()))); // North
        edges.add(Arrays.asList(pos, new TilePosition(pos.x(), pos.y() - 1, pos.z()))); // South
        return edges;
    }

    @Override
    public List<List<ITilePosition>> getVertices(ITilePosition pos) {
        // Each vertex is a set of 3 positions (this tile and two neighbors)
        List<List<ITilePosition>> vertices = new ArrayList<>(4);
        // NW
        vertices.add(Arrays.asList(
            pos,
            new TilePosition(pos.x() - 1, pos.y(), pos.z()),
            new TilePosition(pos.x(), pos.y() + 1, pos.z())
        ));
        // NE
        vertices.add(Arrays.asList(
            pos,
            new TilePosition(pos.x() + 1, pos.y(), pos.z()),
            new TilePosition(pos.x(), pos.y() + 1, pos.z())
        ));
        // SE
        vertices.add(Arrays.asList(
            pos,
            new TilePosition(pos.x() + 1, pos.y(), pos.z()),
            new TilePosition(pos.x(), pos.y() - 1, pos.z())
        ));
        // SW
        vertices.add(Arrays.asList(
            pos,
            new TilePosition(pos.x() - 1, pos.y(), pos.z()),
            new TilePosition(pos.x(), pos.y() - 1, pos.z())
        ));
        return vertices;
    }

    @Override
    public PolygonShape getPolygonShape(ITile tile) {
        // Flat-topped square, normalized to [0,1], center at (0.5, 0.5), side length 1
        float[] vertices = new float[] {
            0f, 0f,   // bottom-left
            1f, 0f,   // bottom-right
            1f, 1f,   // top-right
            0f, 1f    // top-left
        };
        float[] origin = new float[] {0.5f, 0.5f};
        return new PolygonShape(vertices, origin);
    }
}
