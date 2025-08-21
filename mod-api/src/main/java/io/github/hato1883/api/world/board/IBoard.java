package io.github.hato1883.api.world.board;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBoard {
    void setName(String name);
    void setDimensions(Dimension dimension);

    void addTile(ITile tile);
    Collection<ITile> getTiles();
    Map<ITileType, List<ITile>> getTilesGroupedByTileType();
    Optional<ITile> getTile(ITilePosition position);
    int getTileCount();

    Collection<ITile> getNeighbors(ITile tile);

    String getName();
    Dimension getDimensions();

    /**
     * Returns the main grid if the board is homogeneous, or Optional.empty() if heterogeneous.
     */
    Optional<ITileGrid> getGrid();

    /**
     * Returns the grid for a given tile. For homogeneous boards, returns the main grid.
     * For heterogeneous boards, returns the registered grid for the tile's position, or the main grid as fallback.
     */
    ITileGrid getGridForTile(ITile tile);
}
