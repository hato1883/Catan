package io.github.hato1883.core.game.world.board;

import io.github.hato1883.api.world.board.Dimension;
import io.github.hato1883.api.world.board.ICubeCoord;
import io.github.hato1883.api.world.board.IHexTile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class BoardUtils {
    private BoardUtils() {}

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

    public static Dimension computeDimensionsFromTiles(Collection<IHexTile> tiles) {
        if (tiles.isEmpty()) return new Dimension(0, 0, 0);

        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;

        for (IHexTile tile : tiles) {
            ICubeCoord pos = tile.getCoord();
            minX = Math.min(minX, pos.x());
            maxX = Math.max(maxX, pos.x());
            minY = Math.min(minY, pos.y());
            maxY = Math.max(maxY, pos.y());
            minZ = Math.min(minZ, pos.z());
            maxZ = Math.max(maxZ, pos.z());
        }

        int xExtent = maxX - minX + 1;
        int yExtent = maxY - minY + 1;
        int zExtent = maxZ - minZ + 1;
        return new Dimension(xExtent, yExtent, zExtent);
    }
}
