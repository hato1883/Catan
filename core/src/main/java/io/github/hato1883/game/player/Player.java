package io.github.hato1883.game.player;

import com.badlogic.gdx.graphics.Color;
import io.github.hato1883.game.resource.ResourceType;

/**
 * Represents a player in the game.
 * Stores player name and future support for color and score.
 */
public class Player {

    private String name;
    private Color color;  // Currently unused
    private int score;    // Currently unused

    /**
     * Creates a new player with the given color.
     *
     * @param color player's display color
     */
    public Player(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void addResource(ResourceType resource, int i) {
    }

    public Color getColor() {
        return color;
    }
}
