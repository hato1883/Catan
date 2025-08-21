package io.github.hato1883.api.world.board;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

/**
 * Registry for canonical TileVertex objects.
 * Ensures that each unique vertex is only created once.
 */
public class TileVertexRegistry {
    private final Map<TileVertexKey, TileVertex> vertexMap = new ConcurrentHashMap<>();

    public TileVertex getOrCreateVertex(Collection<ITilePosition> tiles) {
        SortedSet<ITilePosition> sorted = new TreeSet<>((a, b) -> {
            int cmp = Float.compare(a.x(), b.x());
            if (cmp != 0) return cmp;
            cmp = Float.compare(a.y(), b.y());
            if (cmp != 0) return cmp;
            return Float.compare(a.z(), b.z());
        });
        sorted.addAll(tiles);
        TileVertexKey key = new TileVertexKey(sorted);
        return vertexMap.computeIfAbsent(key, k -> new TileVertex(sorted));
    }

    // Helper key class for canonicalization
    private static class TileVertexKey {
        private final SortedSet<ITilePosition> tiles;
        TileVertexKey(SortedSet<ITilePosition> tiles) {
            this.tiles = tiles;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TileVertexKey)) return false;
            TileVertexKey key = (TileVertexKey) o;
            return tiles.equals(key.tiles);
        }
        @Override
        public int hashCode() {
            return tiles.hashCode();
        }
    }
}
