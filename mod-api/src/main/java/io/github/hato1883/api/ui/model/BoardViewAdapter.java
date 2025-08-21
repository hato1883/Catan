package io.github.hato1883.api.ui.model;

import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.ITileGrid;
import io.github.hato1883.api.world.board.ITileType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Simple adapter to expose an IBoard as an IBoardView for rendering and UI purposes.
 */
public class BoardViewAdapter implements IBoardView {
    private final IBoard board;

    public BoardViewAdapter(IBoard board) {
        this.board = board;
    }

    @Override
    public Collection<ITile> getTiles() {
        return board.getTiles();
    }

    @Override
    public Map<ITileType, List<ITile>> getTilesGroupedByTileType() {
        return board.getTilesGroupedByTileType();
    }

    @Override
    public Optional<ITileGrid> getGrid() {
        return board.getGrid();
    }

    @Override
    public ITileGrid getGridForTile(ITile tile) {
        return board.getGridForTile(tile);
    }
}
