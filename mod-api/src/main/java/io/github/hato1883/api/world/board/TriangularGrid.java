package io.github.hato1883.api.world.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.sqrt;

/**
 * Implementation of a grid of equilateral triangles.
 * Each tile alternates orientation (up/down) based on its coordinates.
 */
public class TriangularGrid implements ITriangleGrid {
    private static final float EPSILON = 0.01f;
    private static final float SQRT_3 = (float) sqrt(3);
    private static final float TRI_HEIGHT = SQRT_3 / 2f; // height of equilateral triangle with side 1
    private static final float CENTROID_Y_UP = TRI_HEIGHT / 3f; // centroid y for up triangle
    private static final float CENTROID_Y_DOWN = 1f - (TRI_HEIGHT / 3f); // centroid y for down triangle
    private static boolean isUp(ITilePosition pos) {
        // Use yaw to determine orientation: 0 = up, 180 = down
        float yaw = pos.getYaw();
        return Math.abs(yaw) < EPSILON || Math.abs(yaw - 360f) < EPSILON;
    }

    @Override
    public List<ITilePosition> getNeighbors(ITilePosition pos) {
        boolean up = isUp(pos);
        List<ITilePosition> neighbors = new ArrayList<>(3);
        if (up) {
            neighbors.add(new TilePosition(pos.x(), pos.y() - 1, pos.z(), pos.getYaw(), 0f, 0f)); // bottom
            neighbors.add(new TilePosition(pos.x() - 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)); // left
            neighbors.add(new TilePosition(pos.x() + 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)); // right
        } else {
            neighbors.add(new TilePosition(pos.x(), pos.y() + 1, pos.z(), pos.getYaw(), 0f, 0f)); // top
            neighbors.add(new TilePosition(pos.x() - 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)); // left
            neighbors.add(new TilePosition(pos.x() + 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)); // right
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
        boolean up = isUp(pos);
        List<List<ITilePosition>> edges = new ArrayList<>(3);
        if (up) {
            edges.add(Arrays.asList(pos, new TilePosition(pos.x() - 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            edges.add(Arrays.asList(pos, new TilePosition(pos.x() + 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            edges.add(Arrays.asList(pos, new TilePosition(pos.x(), pos.y() - 1, pos.z(), pos.getYaw(), 0f, 0f)));
        } else {
            edges.add(Arrays.asList(pos, new TilePosition(pos.x() - 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            edges.add(Arrays.asList(pos, new TilePosition(pos.x() + 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            edges.add(Arrays.asList(pos, new TilePosition(pos.x(), pos.y() + 1, pos.z(), pos.getYaw(), 0f, 0f)));
        }
        return edges;
    }

    @Override
    public List<List<ITilePosition>> getVertices(ITilePosition pos) {
        boolean up = isUp(pos);
        List<List<ITilePosition>> vertices = new ArrayList<>(3);
        if (up) {
            vertices.add(Arrays.asList(pos, new TilePosition(pos.x() - 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            vertices.add(Arrays.asList(pos, new TilePosition(pos.x() + 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            vertices.add(Arrays.asList(pos, new TilePosition(pos.x(), pos.y() - 1, pos.z(), pos.getYaw(), 0f, 0f)));
        } else {
            vertices.add(Arrays.asList(pos, new TilePosition(pos.x() - 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            vertices.add(Arrays.asList(pos, new TilePosition(pos.x() + 1, pos.y(), pos.z(), pos.getYaw(), 0f, 0f)));
            vertices.add(Arrays.asList(pos, new TilePosition(pos.x(), pos.y() + 1, pos.z(), pos.getYaw(), 0f, 0f)));
        }
        return vertices;
    }

    @Override
    public PolygonShape getPolygonShape(ITile tile) {
        boolean up = isUp(tile.getPosition());
        float[] vertices;
        float[] origin;
        if (up) {
            // Upward equilateral triangle: base (0,0)-(1,0), tip (0.5, TRI_HEIGHT)
            vertices = new float[] {
                0f, 0f,      // left base
                1f, 0f,      // right base
                0.5f, TRI_HEIGHT // tip
            };
            origin = new float[] { 0.5f, CENTROID_Y_UP }; // centroid
        } else {
            // Downward equilateral triangle: base (0,1)-(1,1), tip (0.5, 1-TRI_HEIGHT)
            vertices = new float[] {
                0f, 1f,      // left base
                1f, 1f,      // right base
                0.5f, 1f - TRI_HEIGHT // tip
            };
            origin = new float[] { 0.5f, CENTROID_Y_DOWN }; // centroid
        }
        return new PolygonShape(vertices, origin);
    }
}
