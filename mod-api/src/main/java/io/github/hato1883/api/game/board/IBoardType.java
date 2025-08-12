package io.github.hato1883.api.game.board;

import java.util.Collection;

public interface IBoardType {
    String getId();
    Collection<IHexTile> generateTiles();
}
