package io.github.hato1883.game.player;

import com.badlogic.gdx.graphics.Color;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.IPlayerController;
import io.github.hato1883.api.game.IResourceBank;
import io.github.hato1883.api.game.board.IStructure;

import java.util.List;

/**
 * Represents a player in the game.
 * Stores player name and future support for color and score.
 */
public class Player implements IPlayer {

    // TODO: Add correct fields
    private String name;
    private Color color;  // Currently unused
    private int score;    // Currently unused

    /**
     * Creates a new player with the given color.
     *
     * @param color player's display color
     */
    // TODO: Fix more info
    public Player(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public IPlayerController getController() {
        // TODO:
        return null;
    }

    @Override
    public int getVictoryPoints() {
        // TODO:
        return 0;
    }

    @Override
    public IResourceBank getResourceBank() {
        // TODO:
        return null;
    }

    @Override
    public List<IStructure> getStructures() {
        // TODO:
        return List.of();
    }
}
