package io.github.hato1883.core.ui.gui.adapter;

import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.api.world.board.IHexTile;
import io.github.hato1883.api.world.board.ITileType;
import io.github.hato1883.api.ui.model.IBoardView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class IBoardViewAdapter implements IBoardView {
    private final IBoard board;

    public IBoardViewAdapter(IBoard board) {
        this.board = board;
    }

    @Override
    public Collection<IHexTile> getTiles() {
        return board.getTiles();
    }

    @Override
    public Map<ITileType, List<IHexTile>> getTilesGroupedByTileType() {
        return board.getTilesGroupedByTileType();
    }
}
