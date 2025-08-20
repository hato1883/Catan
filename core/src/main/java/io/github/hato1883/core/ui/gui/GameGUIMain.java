package io.github.hato1883.core.ui.gui;

import com.badlogic.gdx.Game;
import io.github.hato1883.api.*;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.api.world.board.IBoardGenerator;
import io.github.hato1883.core.assets.management.loaders.RenderAssetLoader;
import io.github.hato1883.core.assets.management.loaders.DefaultRenderAssetLoader;
import io.github.hato1883.core.bootstrap.services.FacadeBootstrap;
import io.github.hato1883.core.modloading.loading.ModLoader;
import io.github.hato1883.core.bootstrap.services.ServiceBootstrap;
import io.github.hato1883.core.ui.gui.controls.camera.CameraController;
import io.github.hato1883.core.ui.gui.adapter.IBoardViewAdapter;
import io.github.hato1883.core.ui.gui.rendering.BoardRenderer;
import io.github.hato1883.core.config.RendererConfig;
import io.github.hato1883.core.ui.gui.screen.GameBoardScreen;
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

    @Override
    public void create() {
        LOGGER.info("Starting game!");

        LOGGER.info("Register default services...");
        ServiceBootstrap.initialize();
        LOGGER.info("Default services have been registered");

        LOGGER.info("Setup Facades...");
        FacadeBootstrap.initialize(ServiceBootstrap.getContainer());
        LOGGER.info("Facades have been initialized");

        LOGGER.info("Setting up Mod Loader...");
        loader = new ModLoader();
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


        IBoardGenerator boardGenerator = Services.require(IBoardGenerator.class);
        IBoard board = boardGenerator.generateBoard(Identifier.of("basemod", "classic_hex"), PRNG);

        // Wire abstractions
        RendererConfig cfg = RendererConfig.defaultConfig();
        CameraController camera = new CameraController(cfg);
        BoardRenderer renderer = new BoardRenderer(new IBoardViewAdapter(board), cfg);
        RenderAssetLoader assets = new DefaultRenderAssetLoader("fonts/Roboto-Regular.ttf");

        setScreen(new GameBoardScreen(camera, renderer, assets));
    }
}
