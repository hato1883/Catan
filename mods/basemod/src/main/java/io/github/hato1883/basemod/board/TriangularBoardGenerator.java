package io.github.hato1883.basemod.board;

import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IShapeGenerator;
import io.github.hato1883.api.world.board.ITilePosition;
import io.github.hato1883.api.world.board.TilePosition;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Generates a grid of equilateral triangles, alternating orientation.
 */
public class TriangularBoardGenerator implements IShapeGenerator {
    @Override
    public Set<ITilePosition> generateCoords(BoardGenerationConfig config, Random rng) {
        int width = config.getxExtent();
        int height = config.getyExtent();
        Set<ITilePosition> coords = new HashSet<>();

        float SQRT_3 = (float) Math.sqrt(3);
        float triHeight = SQRT_3 / 2.0f; // Height of equilateral triangle with side 1
        float vertSpacing = triHeight;   // Vertical distance between centroids
        float horizSpacing = 0.5f;       // Horizontal distance between centroids

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean up = (x + y) % 2 == 0; // Alternate orientation
                float x_world = x * horizSpacing;
                float y_world = y * vertSpacing - (up ? triHeight / 3.0f : 0.0f); // Offset for down triangles
                float z_world = 0f;
                float yaw = ((x + y) % 2 == 0) ? 0f : 180f;
                coords.add(new TilePosition(x_world, y_world, z_world, yaw, 0f, 0f));
            }
        }
        return coords;
    }
}
