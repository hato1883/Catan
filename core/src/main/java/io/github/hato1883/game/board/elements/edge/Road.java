package io.github.hato1883.game.board.elements.edge;

import io.github.hato1883.game.board.elements.Structure;
import io.github.hato1883.game.player.Player;

public class Road extends Structure {

    protected Road(Player owner) {
        super(owner);
    }

    public Player getPlayer() {
        return owner;
    }
}
