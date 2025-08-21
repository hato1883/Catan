
package io.github.hato1883.api.world.board;

import java.util.Objects;

/**
 * Canonical representation of an edge between two tile positions.
 * Use TileEdge.of(a, b, context) to ensure canonicalization.
 */
public final class TileEdge {
    private final ITilePosition a;
    private final ITilePosition b;

    TileEdge(ITilePosition a, ITilePosition b) {
        // Always store in canonical order
        if (compare(a, b) <= 0) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    public static TileEdge of(ITilePosition a, ITilePosition b, TileEdgeRegistry registry) {
        return registry.getOrCreateEdge(a, b);
    }

    public ITilePosition getA() { return a; }
    public ITilePosition getB() { return b; }

    static int compare(ITilePosition p1, ITilePosition p2) {
        int cmp = Float.compare(p1.x(), p2.x());
        if (cmp != 0) return cmp;
        cmp = Float.compare(p1.y(), p2.y());
        if (cmp != 0) return cmp;
        return Float.compare(p1.z(), p2.z());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TileEdge edge)) return false;
        return a.equals(edge.a) && b.equals(edge.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
