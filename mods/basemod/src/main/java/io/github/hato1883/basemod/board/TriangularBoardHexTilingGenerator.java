package io.github.hato1883.basemod.board;

import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IShapeGenerator;
import io.github.hato1883.api.world.board.ITilePosition;
import io.github.hato1883.api.world.board.TilePosition;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Generates a board by tiling hexagons made of 6 equilateral triangles.
 * Each hexagon is formed by 6 triangles sharing a vertex at the center.
 * The central hexagon is at (0,0), and 6 more are tiled around it.
 */
public class TriangularBoardHexTilingGenerator implements IShapeGenerator {
    @Override
    public Set<ITilePosition> generateCoords(BoardGenerationConfig config, Random rng) {
        Set<ITilePosition> coords = new HashSet<>();
        float SQRT_3 = (float) Math.sqrt(3);
        float side = 1.0f;
        // Axial coordinates for 7-hex Catan layout (flat-topped)
        int[][] axial = new int[][] {
            {0, 0}, {1, 0}, {0, 1}, {-1, 1}, {-1, 0}, {0, -1}, {1, -1}
        };
        for (int[] hex : axial) {
            int q = hex[0];
            int r = hex[1];
            float x = side * 1.5f * q;
            float y = side * SQRT_3 * (r + q / 2.0f);
            addHexagon(coords, new float[]{x, y}, true);
        }
        return coords;
    }

    /**
     * Adds 6 triangles forming a hexagon at the given center.
     * Each triangle shares a vertex at (center[0], center[1]).
     * The 'up' parameter determines the orientation of the hexagon.
     */
    private void addHexagon(Set<ITilePosition> coords, float[] center, boolean up) {
        float cx = center[0];
        float cy = center[1];
        float side = 1.0f;
        // For each triangle, place it so that one vertex is at the center,
        // and the other two are at the corners of the hexagon.
        for (int i = 0; i < 6; i++) {
            // Angle for this triangle's outer vertex
            double angle1 = Math.PI / 3 * i;
            double angle2 = Math.PI / 3 * (i + 1);
            // The centroid of the triangle (for tile position)
            float x1 = cx + side * (float) Math.cos(angle1);
            float y1 = cy + side * (float) Math.sin(angle1);
            float x2 = cx + side * (float) Math.cos(angle2);
            float y2 = cy + side * (float) Math.sin(angle2);
            // The centroid of the triangle (average of the three vertices)
            float tx = (cx + x1 + x2) / 3f;
            float ty = (cy + y1 + y2) / 3f;
            float z = 0f;
            // Orientation: alternate 0/180, but flip if hex is 'down'
            float yaw = (i % 2 == 0) == up ? 0f : 180f;
            coords.add(new TilePosition(tx, ty, z, yaw, 0f, 0f));
        }
    }
}
