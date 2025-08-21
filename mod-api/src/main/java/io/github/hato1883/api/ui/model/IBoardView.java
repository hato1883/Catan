package io.github.hato1883.api.ui.model;

import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.ITileType;
import io.github.hato1883.api.world.board.ITileGrid;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBoardView {
    // Flat list if you need iteration
    Collection<ITile> getTiles();

    // Grouped by tile type for efficient texture lookup
    Map<ITileType, List<ITile>> getTilesGroupedByTileType();

    /**
     * Returns the grid used for this board, if homogeneous. If the board is heterogeneous, returns Optional.empty().
     */
    Optional<ITileGrid> getGrid();

    /**
     * Returns the grid for the given tile. For homogeneous boards, this can return the same grid for all tiles.
     */
    ITileGrid getGridForTile(ITile tile);
}
