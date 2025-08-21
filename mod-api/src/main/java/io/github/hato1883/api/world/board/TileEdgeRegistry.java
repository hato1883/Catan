package io.github.hato1883.api.world.board;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for canonical TileEdge objects.
 * Ensures that each unique edge is only created once.
 */
public class TileEdgeRegistry {
    private final Map<TileEdgeKey, TileEdge> edgeMap = new ConcurrentHashMap<>();

    public TileEdge getOrCreateEdge(ITilePosition a, ITilePosition b) {
        TileEdgeKey key = new TileEdgeKey(a, b);
        return edgeMap.computeIfAbsent(key, k -> new TileEdge(a, b));
    }

    // Helper key class for canonicalization
    private static class TileEdgeKey {
        private final ITilePosition a, b;
        TileEdgeKey(ITilePosition a, ITilePosition b) {
            if (TileEdge.compare(a, b) <= 0) {
                this.a = a; this.b = b;
            } else {
                this.a = b; this.b = a;
            }
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TileEdgeKey)) return false;
            TileEdgeKey key = (TileEdgeKey) o;
            return a.equals(key.a) && b.equals(key.b);
        }
        @Override
        public int hashCode() {
            return 31 * a.hashCode() + b.hashCode();
        }
    }
}
