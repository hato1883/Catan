package io.github.hato1883.basemod;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.Services;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.ui.model.RendererConfig;
import io.github.hato1883.api.ui.render.IBoardRenderer;
import io.github.hato1883.api.world.board.IBoardType;
import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.IModRegistrar;
import io.github.hato1883.api.events.ui.UIOverlayRenderEvent;
import io.github.hato1883.api.Events;
import io.github.hato1883.api.ui.screen.ScreenRegistry;
import io.github.hato1883.api.ui.screen.IScreenManager;
import io.github.hato1883.basemod.board.*;
import io.github.hato1883.basemod.screen.MainMenuScreen;
import io.github.hato1883.basemod.screen.GameScreen;

import static io.github.hato1883.api.LogManager.*;

public class BaseModMain implements CatanMod {

    public final static String MOD_ID = "basemod";

    private static boolean toggleButtonAdded = false;

    @Override
    public void registerModContent(IModRegistrar registrar) {
        getLogger(MOD_ID).info("registerModContent called");
        CatanMod.super.registerModContent(registrar);
        getLogger(MOD_ID).info("Registering different board types....");
        registerBoard(new DonutBoardType());
        registerBoard(new ClassicHexIslandBoard());
        registerBoard(new ShuffledHexIslandBoard());
        registerBoard(new SquareBoardType());
        registerBoard(new TriangularBoardType());
        getLogger(MOD_ID).info("Board types has been registered");

        getLogger(MOD_ID).info("Registering Default board renderer....");
        // Services.register(IBoardRenderer.class, new CanonicalBoardRenderer(RendererConfig.defaultConfig()));
        getLogger(MOD_ID).info("Default board renderer has been registered");

        Registries.registerUIBatchingJob(
            Identifier.of(MOD_ID, "test_effect"),
            new AnimatedCircleBatchingJob(150, 40, 0.02f)
        );

        getLogger(MOD_ID).info("Registering UIOverlayRenderEvent listener...");
        // Register UI overlay event listener for toggle button
        Events.registerListener(MOD_ID, UIOverlayRenderEvent.class, EventPriority.NORMAL, event -> {
            getLogger(MOD_ID).info("UIOverlayRenderEvent listener triggered");
            if (!toggleButtonAdded) {
                Stage stage = event.getStage();
                TextButton button = new TextButton("Toggle Red Ball", event.getSkin());
                // Make the button large for visibility
                button.setSize(400, 120);
                // Place button in the center of the screen for visibility
                button.setPosition(stage.getWidth() / 2f - button.getWidth() / 2f, stage.getHeight() / 2f - button.getHeight() / 2f);
                button.getLabel().setFontScale(2.5f);
                button.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                        AnimatedCircleBatchingJob.showRedBall = !AnimatedCircleBatchingJob.showRedBall;
                        button.setText(AnimatedCircleBatchingJob.showRedBall ? "Hide Red Ball" : "Show Red Ball");
                    }
                });
                button.setText(AnimatedCircleBatchingJob.showRedBall ? "Hide Red Ball" : "Show Red Ball");
                stage.addActor(button);
                getLogger(MOD_ID).info("Toggle button added to UI stage at position: ({} , {}), size: ({} x {})", button.getX(), button.getY(), button.getWidth(), button.getHeight());
                toggleButtonAdded = true;
            }
        });
        getLogger(MOD_ID).info("UIOverlayRenderEvent listener registered");

        // Register screens with the ScreenRegistry
        ScreenRegistry screenRegistry = Services.require(ScreenRegistry.class);
        screenRegistry.registerScreen(MainMenuScreen.ID, new MainMenuScreen());
        screenRegistry.registerScreen(GameScreen.ID, new GameScreen());
        // Set main menu as default screen
        IScreenManager screenManager = Services.require(IScreenManager.class);
        screenManager.setDefaultScreen(GameScreen.ID);
        getLogger(MOD_ID).info("Registered MainMenuScreen and GameScreen with ScreenRegistry");
    }

    private void registerBoard(IBoardType type) {
        Registries.register(type);
    }

    @Override
    public void onInitialize() {
        getLogger(MOD_ID).info("Mod is loaded!");
        getLogger(MOD_ID).info("Registering resource...");
        BaseCatanResources.initialize();
        getLogger(MOD_ID).info("Resource has been registered");

        getLogger(MOD_ID).info("Registering tiles...");
        BaseCatanTiles.initialize();
        getLogger(MOD_ID).info("Tiles has been registered");
    }

    @Override
    public void onLoad() {
        CatanMod.super.onLoad();
    }

    @Override
    public void onEnable() {
        CatanMod.super.onEnable();
    }

    @Override
    public void onDisable() {
        CatanMod.super.onDisable();
    }
}
