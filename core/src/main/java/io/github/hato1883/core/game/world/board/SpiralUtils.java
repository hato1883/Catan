package io.github.hato1883.core.game.world.board;

import io.github.hato1883.api.world.board.ICubeCoord;

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
        if (coords.isEmpty()) return spiral;

        // Choose axis direction ONCE
        int axisDirection = rng != null ? rng.nextInt(6) : 0;

        // Group coords by radius
        Map<Integer, List<ICubeCoord>> byRadius = new HashMap<>();
        int maxRadius = 0;
        for (ICubeCoord c : coords) {
            int r = c.distanceFromOrigin();
            byRadius.computeIfAbsent(r, k -> new ArrayList<>()).add(c);
            maxRadius = Math.max(maxRadius, r);
        }

        // Outer to inner
        for (int r = maxRadius; r >= 0; r--) {
            List<ICubeCoord> ring = byRadius.getOrDefault(r, Collections.emptyList());
            if (ring.isEmpty()) continue;

            // Sort ring in clockwise order starting from extreme in axisDirection
            ICubeCoord start = findExtremeInDirection(ring, DIRS[axisDirection]);
            List<ICubeCoord> sorted = sortClockwise(ring, start);

            spiral.addAll(sorted);
        }
        return spiral;
    }

    // Finds the coord farthest in given direction
    private static ICubeCoord findExtremeInDirection(Collection<ICubeCoord> coords, ICubeCoord dir) {
        return coords.stream()
            .max(Comparator.comparingInt(c -> c.dot(dir))) // dot product works for cube coords
            .orElseThrow();
    }

    // Sort clockwise from start coord
    private static List<ICubeCoord> sortClockwise(List<ICubeCoord> ring, ICubeCoord start) {
        if (ring.size() <= 1) return new ArrayList<>(ring);

        // Sort by polar angle in cube-coord space around (0,0,0)
        ICubeCoord origin = CubeCoord.of(0,0,0); // adjust to your cube coord class
        List<ICubeCoord> sorted = new ArrayList<>(ring);
        sorted.sort(Comparator.comparingDouble(c -> {
            // pick any 2D projection
            return Math.atan2(c.z(), c.x());
        }));

        // Rotate list so start is first
        int idx = sorted.indexOf(start);
        if (idx > 0) {
            Collections.rotate(sorted, -idx);
        }
        return sorted;
    }
}
