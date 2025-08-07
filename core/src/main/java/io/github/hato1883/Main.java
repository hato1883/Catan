package io.github.hato1883;

import com.badlogic.gdx.Game;
import io.github.hato1883.ui.gui.GameBoard;

import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    /**
     * Shared pseudo-random number generator used across the game logic.
     */
    public static final Random PRNG = new Random();

    @Override
    public void create() {
        setScreen(new GameBoard(this));
    }
}
