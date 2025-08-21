package io.github.hato1883.basemod.screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.ui.screen.AbstractEventDrivenScreen;
import io.github.hato1883.api.Services;
import io.github.hato1883.api.ui.model.IBoardView;
import io.github.hato1883.api.assets.IAssetProvider;
import io.github.hato1883.api.ui.render.IBoardRenderer;
import io.github.hato1883.api.world.board.BoardProvider;
import io.github.hato1883.api.world.board.IBoard;
import io.github.hato1883.api.world.board.IBoardGenerator;
import io.github.hato1883.core.assets.management.loaders.RenderAssetLoader;
import io.github.hato1883.core.assets.management.loaders.DefaultRenderAssetLoader;
import io.github.hato1883.core.assets.AssetProvider;
import io.github.hato1883.basemod.board.CanonicalBoardRenderer;
import io.github.hato1883.api.ui.model.RendererConfig;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import static io.github.hato1883.api.LogManager.getLogger;
import static io.github.hato1883.basemod.BaseModMain.MOD_ID;

/**
 * The main game screen for the base mod. Uses the identifier "basemod:game_screen".
 */
public class GameScreen extends AbstractEventDrivenScreen {
    private IBoardRenderer boardRenderer;
    private IBoardView boardView;
    private RenderAssetLoader renderAssetLoader;
    private IAssetProvider assetProvider;
    private boolean assetsLoaded = false;

    // Zoom configuration
    private static final float ZOOM_SPEED = 1.5f;

    public static final Identifier ID = Identifier.of("basemod", "game_screen");

    private RendererConfig rendererConfig;

    private SpriteBatch debugBatch;
    private Texture debugTexture;

    public GameScreen() {
        super(ID);
    }

    @Override
    protected void onShow() {
        getLogger(MOD_ID).info("Game Screen is now visible");
        // Create renderer config and pass to renderer
        rendererConfig = RendererConfig.defaultConfig();
        boardRenderer = new CanonicalBoardRenderer(rendererConfig);
        // Generate the board and set it in the BoardProvider
        IBoardGenerator boardGenerator = Services.require(IBoardGenerator.class);
        IBoard board = boardGenerator.generateBoard(Identifier.of("basemod", "triangular_grid"), null);
        // Set the board in the provider
        BoardProvider boardProvider = Services.require(BoardProvider.class);
        boardProvider.setBoard(board);
        // Fetch the up-to-date IBoardView
        boardView = Services.require(IBoardView.class);
        // Use DefaultRenderAssetLoader with font path
        renderAssetLoader = new DefaultRenderAssetLoader("fonts/Roboto-Regular.ttf");
        renderAssetLoader.queueAssets();
        assetProvider = null;
        assetsLoaded = false;

        // Register for asset upgrades (no instanceof needed)
        boardRenderer.registerAssetUpgradeNotifier(renderAssetLoader.getTextureUpgradeNotifier());

        // Camera initialization for correct viewport and position
        getCamera().setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Center camera on board
        getCamera().position.set(0, 0, 0);
        // centerCameraOnBoard();
        getCamera().zoom = 1.0f;
        getCamera().update();

        // Debug: create a 1x1 white texture for drawing
        debugBatch = new SpriteBatch();
        debugTexture = new Texture(1, 1, Pixmap.Format.RGBA8888);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        debugTexture.draw(pixmap, 0, 0);
        pixmap.dispose();
    }

    @Override
    protected void onRender(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // --- Camera zoom logic ---
        float zoomFactor = (float) Math.pow(ZOOM_SPEED, Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            getCamera().zoom /= zoomFactor;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            getCamera().zoom *= zoomFactor;
        }
        getCamera().zoom = Math.max(rendererConfig.minZoom(), Math.min(rendererConfig.maxZoom(), getCamera().zoom));
        getCamera().update();
        // Always update asset loader to process LOD upgrades
        renderAssetLoader.update();
        // Create assetProvider and initialize sprites as soon as possible
        if (assetProvider == null) {
            assetProvider = new AssetProvider(renderAssetLoader);
            boardRenderer.initializeSprites(assetProvider, boardView);
        }
        // Optionally show loading bar if not done
        if (!assetsLoaded) {
            if (renderAssetLoader.update()) {
                assetsLoaded = true;
            } else {
                renderAssetLoader.renderLoading();
            }
        }
        // Always render the board, even if assets are not fully loaded
        boardRenderer.render(getCamera(), boardView, assetProvider);
    }

    @Override
    protected void onDispose() {
        if (boardRenderer != null) boardRenderer.dispose();
        if (renderAssetLoader != null) renderAssetLoader.dispose();
        if (debugBatch != null) debugBatch.dispose();
        if (debugTexture != null) debugTexture.dispose();
    }
}
