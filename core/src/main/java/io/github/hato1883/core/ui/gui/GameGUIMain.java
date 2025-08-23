package io.github.hato1883.core.ui.gui;

import com.badlogic.gdx.Game;
import io.github.hato1883.api.*;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.ui.screen.IScreenManager;
import io.github.hato1883.api.ui.screen.ScreenRegistry;
import io.github.hato1883.core.modloading.loading.ModLoader;
import io.github.hato1883.core.bootstrap.services.ServiceBootstrap;
import io.github.hato1883.core.common.util.PathResolver;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Random;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameGUIMain extends Game {

    static {
        Logger LOGGER = LogManager.getLogger("");
    }
    public static final String GAME_NAME = "Catan";
    public static final String LOGGER_ID = "";
    private static final Logger LOGGER = LogManager.getLogger(LOGGER_ID);
    private ModLoader loader;

    /**
     * Shared pseudo-random number generator used across the game logic.
     */
    public static final Random PRNG = new Random();

    private Identifier currentScreenId = null;
    private IScreenManager screenManager;
    private io.github.hato1883.api.ui.screen.ScreenRegistry screenRegistry;

    @Override
    public void create() {
        LOGGER.info("Starting game!");

        LOGGER.info("Register default services...");
        ServiceBootstrap.initialize();
        LOGGER.info("Default services have been registered");

        LOGGER.info("Setting up Mod Loader...");
        loader = ModLoader.createDefault();
        LOGGER.info("Mod Loader setup completed");

        // Register default events
        LOGGER.info("Registering base game event Listeners...");
        Services.require(IEventListenerRegistrar.class).registerListenersInPackage(
            LOGGER_ID,
            "io.github.hato1883.game.logic.listeners"
        );
        LOGGER.info("Base game event Listeners has be registered!");

        LOGGER.info("Loading Catan mods...");
        try {
            loader.loadAll(PathResolver.getGameDataDir().resolve("mods"));
        } catch (IOException e) {
            LOGGER.error("Critical IO error while loading mods: {}", e.getMessage(), e);
            System.exit(1);
        }
        LOGGER.info("All Catan mods have been loaded!");

        screenRegistry = Services.require(ScreenRegistry.class);
        screenManager = Services.require(IScreenManager.class);
        screenManager.showDefaultScreen();
    }

    @Override
    public void render() {
        Identifier newScreenId = screenManager.getCurrentScreenId();
        if (newScreenId != null && !newScreenId.equals(currentScreenId)) {
            // Switch screens using LibGDX setScreen
            var newScreen = screenRegistry.getScreen(newScreenId);
            if (newScreen != null) {
                setScreen(newScreen); // LibGDX will call show(), hide(), etc.
                currentScreenId = newScreenId;
            }
        }
        super.render(); // Let LibGDX handle rendering the current screen
    }
}
