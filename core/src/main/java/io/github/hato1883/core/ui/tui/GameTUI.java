package io.github.hato1883.core.ui.tui;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.Services;
import io.github.hato1883.api.events.IEventListenerRegistrar;
import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.api.ui.IUI;
import io.github.hato1883.api.ui.TUIScreen;
import io.github.hato1883.core.modloading.loading.ModLoader;
import io.github.hato1883.core.bootstrap.services.ServiceBootstrap;
import io.github.hato1883.core.ui.tui.screen.BoardCreationTUIScreen;
import io.github.hato1883.core.ui.tui.screen.GamePlayTUIScreen;
import io.github.hato1883.core.ui.tui.screen.MainMenuTUIScreen;
import io.github.hato1883.core.common.util.PathResolver;
import org.slf4j.Logger;

import java.io.IOException;

import static java.lang.System.exit;

public class GameTUI implements IUI {
    public static final String GAME_NAME = "Catan";
    public static final String LOGGER_ID = "";
    private static final Logger LOGGER = LogManager.getLogger(LOGGER_ID);
    private final ModLoader loader;
    private final IServiceContainer serviceLocator;

    private TUIScreen currentScreen;

    public GameTUI() {
        LOGGER.info("Register default services...");
        ServiceBootstrap.initialize();
        LOGGER.info("Default services have been registered");
        loader = ModLoader.createDefault(ServiceBootstrap.getContainer());
        serviceLocator = ServiceBootstrap.getContainer();
    }

    @Override
    public void create() {
        LOGGER.info("Starting game!");

        // Register default events
        LOGGER.info("Registering base game event Listeners...");
        serviceLocator.get(IEventListenerRegistrar.class).orElseThrow(() -> new IllegalStateException("Required service not found: IEventListenerRegistrar")).registerListenersInPackage(
            LOGGER_ID,
            "io.github.hato1883.game.logic.listeners"
        );
        LOGGER.info("Base game event Listeners has be registered!");

        LOGGER.info("Loading Catan mods...");
        try {
            loader.loadAll(PathResolver.getGameDataDir().resolve("mods"));
        } catch (IOException e) {
            LOGGER.error("Critical IO error while loading mods: {}", e.getMessage(), e);
            exit(1);
        }
        LOGGER.info("All Catan mods have been loaded!");

        // setScreen(new BoardCreationTUIScreen());
        TUIInput.start();
        setScreen(new MainMenuTUIScreen());
        runLoop();
    }

    private void setScreen(TUIScreen screen) {
        if (currentScreen != null) {
            currentScreen.hide();
        }
        currentScreen = screen;
        currentScreen.show();
    }

    public void render() {
        if (currentScreen != null) {
            currentScreen.render();

            if (currentScreen.isFinished()) {
                if (currentScreen instanceof MainMenuTUIScreen) {
                    String choice = ((MainMenuTUIScreen) currentScreen).getSelectedOption();
                    System.out.println("User sent \"" + choice + "\"");
                    switch (choice) {
                        case "StartGame":
                            setScreen(new BoardCreationTUIScreen(serviceLocator));
                            break;
                        case "LoadGame":
                            // setScreen(new LoadGameTUIScreen());
                            break;
                        case "Exit":
                            exit(0);
                            break;
                    }
                } else if (currentScreen instanceof BoardCreationTUIScreen) {
                    IBoard board = ((BoardCreationTUIScreen) currentScreen).getBoard();
                    // Now you can pass this board to a GamePlayTUIScreen
                    if (board != null)
                        setScreen(new GamePlayTUIScreen(board));
                } else if (currentScreen instanceof GamePlayTUIScreen) {
                    exit(0);
                }
            }
        }
    }

    private void runLoop() {
        while (true) {
            render();;
        }
    }
}
