package io.github.hato1883.api.world.board;

import java.util.Collections;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Collection;

/**
 * Canonical representation of a vertex shared by a set of tile positions.
 * Use TileVertex.of(tiles, registry) to ensure canonicalization.
 */
public final class TileVertex {
    private final SortedSet<ITilePosition> tiles;

    TileVertex(SortedSet<ITilePosition> tiles) {
        this.tiles = Collections.unmodifiableSortedSet(tiles);
    }

    public static TileVertex of(Collection<ITilePosition> tiles, TileVertexRegistry registry) {
        return registry.getOrCreateVertex(tiles);
    }

    public SortedSet<ITilePosition> getTiles() { return tiles; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TileVertex vertex)) return false;
        return tiles.equals(vertex.tiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tiles);
    }
}
