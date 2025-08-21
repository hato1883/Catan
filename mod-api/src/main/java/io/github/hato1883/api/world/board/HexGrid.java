package io.github.hato1883.api.world.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Pointy-topped hex grid implementation for Catan-style boards.
 * Uses cube coordinates (x, y, z) with x + y + z = 0.
 */
public class HexGrid implements IHexGrid {
    // Cube coordinate neighbor deltas for pointy-topped hexes
    /** Neighbor deltas for pointy-topped hex grid:
     * <pre>{@code
     *             __---*---__ __---*---__
     *            *           *           *
     *            |           |           |
     *            |{0, -1, +1}|{+1, -1, 0}|
     *            |           |           |
     *      ___---*---__ __---*---__ __---*---__
     *      *           *           *           *
     *      |           |           |           |
     *      |{-1, 0, +1}| {0, 0, 0} |{+1, 0, -1}|
     *      |           |           |           |
     *      *---__ __---*---__ __---*---__ __---*
     *            *           *           *
     *            |           |           |
     *            |{-1, +1, 0}|{0, +1, -1}|
     *            |           |           |
     *            *---__ __---*---__ __---*
     *                  *           *
     *}</pre>
     **/
    private static final int[][] NEIGHBOR_DELTAS = {
        {+1, -1, 0}, {+1, 0, -1}, {0, +1, -1},
        {-1, +1, 0}, {-1, 0, +1}, {0, -1, +1}
    };

    @Override
    public List<ITilePosition> getNeighbors(ITilePosition pos) {
        List<ITilePosition> neighbors = new ArrayList<>(6);
        for (int[] d : NEIGHBOR_DELTAS) {
            neighbors.add(new TilePosition(pos.x() + d[0], pos.y() + d[1], pos.z() + d[2]));
        }
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
        List<List<ITilePosition>> edges = new ArrayList<>(6);
        for (int[] d : NEIGHBOR_DELTAS) {
            ITilePosition neighbor = new TilePosition(pos.x() + d[0], pos.y() + d[1], pos.z() + d[2]);
            edges.add(Arrays.asList(pos, neighbor));
        }
        return edges;
    }

    @Override
    public List<List<ITilePosition>> getVertices(ITilePosition pos) {
        // Each vertex is defined by this tile and two adjacent neighbors
        List<List<ITilePosition>> vertices = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            int[] d1 = NEIGHBOR_DELTAS[i];
            int[] d2 = NEIGHBOR_DELTAS[(i + 1) % 6];
            ITilePosition n1 = new TilePosition(pos.x() + d1[0], pos.y() + d1[1], pos.z() + d1[2]);
            ITilePosition n2 = new TilePosition(pos.x() + d2[0], pos.y() + d2[1], pos.z() + d2[2]);
            vertices.add(Arrays.asList(pos, n1, n2));
        }
        return vertices;
    }

    @Override
    public PolygonShape getPolygonShape(ITile tile) {
        // Pointy-topped hexagon, normalized to [0,1], center at (0.5, 0.5), radius 0.5
        float[] vertices = new float[12];
        double centerX = 0.5, centerY = 0.5, radius = 0.5;
        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60 * i - 30); // Pointy top
            vertices[2 * i] = (float) (centerX + radius * Math.cos(angleRad));
            vertices[2 * i + 1] = (float) (centerY + radius * Math.sin(angleRad));
        }
        float[] origin = new float[] {0.5f, 0.5f};
        return new PolygonShape(vertices, origin);
    }
}
