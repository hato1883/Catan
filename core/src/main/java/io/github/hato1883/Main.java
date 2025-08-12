package io.github.hato1883;

import com.badlogic.gdx.Game;
import io.github.hato1883.game.event.EventListenerRegistrar;
import io.github.hato1883.modloader.ModLoader;
import io.github.hato1883.ui.gui.GameBoard;
import io.github.hato1883.api.LogManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final String GAME_NAME = "Catan";
    public static final String LOGGER_ID = "";
    private static final Logger LOGGER = LogManager.getLogger(LOGGER_ID);
    private final ModLoader loader = new ModLoader();

    /**
     * Shared pseudo-random number generator used across the game logic.
     */
    public static final Random PRNG = new Random();

    @Override
    public void create() {
        LOGGER.info("Starting game!");
        LogManager.getLogger("").info("Starting game!");

        // Register default events
        LOGGER.info("Registering base game event Listeners...");
        EventListenerRegistrar.registerListenersInPackage(
            LOGGER_ID,
            "io.github.hato1883.game.logic.listeners"
        );
        LOGGER.info("Base game event Listeners has be registered!");

        LOGGER.info("Loading Catan mods...");
        try {
            // Load mods from <GameData>/mods folder (seq)
            // Register their listeners (async)
            // Call all mods onInit() (seq)
            loader.loadAll();
        } catch (IOException e) {
            LOGGER.error("Critical IO error while loading mods: {}", e.getMessage(), e);
            System.exit(1);
        }
        LOGGER.info("All catan mods have been loaded!");
        setScreen(new GameBoard(this));
    }
}
