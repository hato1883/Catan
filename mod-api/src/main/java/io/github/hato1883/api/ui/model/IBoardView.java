package io.github.hato1883.api.ui.model;

import io.github.hato1883.api.world.board.IHexTile;
import io.github.hato1883.api.world.board.ITileType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IBoardView {
    // Flat list if you need iteration
    Collection<IHexTile> getTiles();

    // Grouped by tile type for efficient texture lookup
    Map<ITileType, List<IHexTile>> getTilesGroupedByTileType();
}
