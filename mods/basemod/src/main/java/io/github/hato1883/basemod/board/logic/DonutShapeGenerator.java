package io.github.hato1883.basemod.board.logic;

import io.github.hato1883.api.world.board.BoardGenerationConfig;
import io.github.hato1883.api.world.board.IShapeGenerator;
import io.github.hato1883.api.world.board.ITilePosition;
import io.github.hato1883.api.world.board.TilePosition;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DonutShapeGenerator implements IShapeGenerator {
    @Override
    public Set<ITilePosition> generateCoords(BoardGenerationConfig config, Random rng) {
        int outerRadius = Math.min(config.getxExtent(), Math.min(config.getyExtent(), config.getzExtent())) / 2 + 1;
        int innerRadius = 2;
        Set<ITilePosition> coords = new HashSet<>();
        for (int x = -outerRadius; x <= outerRadius; x++)
            for (int y = -outerRadius; y <= outerRadius; y++) {
                int z = -x - y;
                int distanceFromCenter = (Math.abs(x) + Math.abs(y) + Math.abs(z)) / 2;
                if (distanceFromCenter >= innerRadius && distanceFromCenter < outerRadius) {
                    coords.add(new TilePosition(x, y, z));
                }
            }
        // Debug: Print all generated coordinates
        System.out.println("DonutShapeGenerator generated coords:");
        for (ITilePosition pos : coords) {
            System.out.println("Cube: (" + pos.x() + ", " + pos.y() + ", " + pos.z() + ")");
        }
        return coords;
    }
}
