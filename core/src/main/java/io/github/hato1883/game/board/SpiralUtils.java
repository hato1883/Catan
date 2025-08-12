package io.github.hato1883.game.board;

import io.github.hato1883.api.game.board.ICubeCoord;

import java.util.*;

public class SpiralUtils {

    private static final ICubeCoord[] DIRS = {
        new CubeCoord(1, -1, 0),
        new CubeCoord(1, 0, -1),
        new CubeCoord(0, 1, -1),
        new CubeCoord(-1, 1, 0),
        new CubeCoord(-1, 0, 1),
        new CubeCoord(0, -1, 1)
    };

    public static List<ICubeCoord> spiralOrder(Set<ICubeCoord> coords, Random rng) {
        List<ICubeCoord> spiral = new ArrayList<>();
        Set<ICubeCoord> remaining = new HashSet<>(coords);

        // Choose a starting axis ONCE
        int axisDirection = rng.nextInt(6);

        while (!remaining.isEmpty()) {
            List<ICubeCoord> ring = findOuterRing(remaining, axisDirection);
            spiral.addAll(ring);
            ring.forEach(remaining::remove);
        }
        return spiral;
    }

    private static List<ICubeCoord> findOuterRing(Set<ICubeCoord> coords, int axisDirection) {
        ICubeCoord start = findExtremeInDirection(coords, DIRS[axisDirection]);
        int radius = start.distanceFromOrigin();

        List<ICubeCoord> ring = new ArrayList<>();
        ICubeCoord current = start;
        ICubeCoord prev = null;

        do {
            ring.add(current);
            ICubeCoord next = getNextClockwise(current, prev, coords, radius);
            prev = current;
            current = next;
        } while (!current.equals(start));

        return ring;
    }

    private static ICubeCoord getNextClockwise(ICubeCoord current, ICubeCoord prev, Set<ICubeCoord> coords, int radius) {
        int startDir = 0;
        if (prev != null) {
            ICubeCoord diff = prev.subtract(current);
            for (int i = 0; i < DIRS.length; i++) {
                if (DIRS[i].equals(diff)) {
                    startDir = (i + 4) % 6; // Opposite side
                    break;
                }
            }
        }

        for (int i = 0; i < DIRS.length; i++) {
            int dirIndex = (startDir + i) % 6;
            ICubeCoord neighbor = current.add(DIRS[dirIndex]);

            // Must be in same radius shell & in coords
            if (coords.contains(neighbor) && neighbor.distanceFromOrigin() == radius) {
                return neighbor;
            }
        }

        throw new IllegalStateException("No next clockwise neighbor found for " + current);
    }

    private static ICubeCoord findExtremeInDirection(Set<ICubeCoord> coords, ICubeCoord dir) {
        return coords.stream()
            .max(Comparator.comparingInt(c -> c.x() * dir.x() + c.y() * dir.y() + c.z() * dir.z()))
            .orElseThrow();
    }
}
