package io.github.hato1883.core.ui.gui;

import com.badlogic.gdx.Game;
import io.github.hato1883.api.*;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.game.board.IBoard;
import io.github.hato1883.api.game.board.IBoardGenerator;
import io.github.hato1883.api.service.IServiceLocator;
import io.github.hato1883.core.assets.RenderAssetLoader;
import io.github.hato1883.core.assets.DefaultRenderAssetLoader;
import io.github.hato1883.core.modloading.ModLoader;
import io.github.hato1883.core.service.ServiceBootstrap;
import io.github.hato1883.core.ui.gui.camera.CameraController;
import io.github.hato1883.core.ui.gui.model.IBoardViewAdapter;
import io.github.hato1883.core.ui.gui.render.BoardRenderer;
import io.github.hato1883.core.ui.gui.render.RendererConfig;
import io.github.hato1883.core.ui.gui.screen.GameBoardScreen;
import io.github.hato1883.core.util.PathResolver;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Random;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

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
        IServiceLocator provider = ServiceBootstrap.getProvider();
        LOGGER.info("Default services have been registered");

        LOGGER.info("Setup Modding interfaces...");
        ModServices.initialize(provider);
        LOGGER.info("Modding interfaces setup completed");

        LOGGER.info("Setting up Factory interfaces...");
        Factories.initialize(provider);
        LOGGER.info("Factory interface setup completed");

        LOGGER.info("Setting up Registry interfaces...");
        Registries.initialize(provider);
        LOGGER.info("Registry interface setup completed");

        LOGGER.info("Setting up Event interfaces...");
        Events.initialize(provider);
        LOGGER.info("Event interface setup completed");

        LOGGER.info("Setting up Mod Loader...");
        loader = new ModLoader();
        LOGGER.info("Mod Loader setup completed");

        // Register default events
        LOGGER.info("Registering base game event Listeners...");
        provider.requireService(IEventListenerRegistrar.class).registerListenersInPackage(
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


        IBoardGenerator boardGenerator = provider.requireService(IBoardGenerator.class);
        IBoard board = boardGenerator.generateBoard(Identifier.of("basemod", "classic_hex"), PRNG);

        // Wire abstractions
        RendererConfig cfg = RendererConfig.defaultConfig();
        CameraController camera = new CameraController(cfg);
        BoardRenderer renderer = new BoardRenderer(new IBoardViewAdapter(board), cfg);
        RenderAssetLoader assets = new DefaultRenderAssetLoader("fonts/Roboto-Regular.ttf");

        setScreen(new GameBoardScreen(camera, renderer, assets));
    }
}
