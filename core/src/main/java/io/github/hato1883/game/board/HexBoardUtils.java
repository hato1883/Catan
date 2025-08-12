package io.github.hato1883.game.board;

import io.github.hato1883.api.game.board.ICubeCoord;

import java.util.HashSet;
import java.util.Set;

public final class HexBoardUtils {
    private HexBoardUtils() {}

    /**
     * Generates cube coordinates for a hexagonal map with given radius.
     * Radius 1 means a single tile, radius 2 is 3 tiles wide, etc.
     */
    public static Set<ICubeCoord> generateHexCoords(int radius) {
        Set<ICubeCoord> coords = new HashSet<>();
        for (int x = -radius + 1; x <= radius - 1; x++) {
            for (int y = -radius + 1; y <= radius - 1; y++) {
                int z = -x - y;
                if (Math.abs(z) < radius) {
                    coords.add(new CubeCoord(x, y, z));
                }
            }
        }
        return coords;
    }
}
