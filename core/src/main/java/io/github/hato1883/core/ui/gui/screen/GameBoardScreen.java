package io.github.hato1883.core.ui.gui.screen;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.hato1883.core.assets.management.loaders.RenderAssetLoader;
import io.github.hato1883.core.ui.gui.rendering.BoardRenderer;
import io.github.hato1883.core.ui.gui.controls.camera.CameraController;

/**
 * Application layer: coordinates screen lifecycle and delegates to collaborators.
 * SRP: owns lifecycle only. DIP: depends on abstractions (AssetLoader, BoardRenderer, BoardView, CameraController).
 */
public final class GameBoardScreen implements Screen {
    private final CameraController cameraController;
    private final BoardRenderer boardRenderer;
    private final RenderAssetLoader renderAssetLoader;
    private boolean assetsLoaded;

    public GameBoardScreen(CameraController cameraController,
                           BoardRenderer boardRenderer,
                           RenderAssetLoader renderAssetLoader) {
        this.cameraController = cameraController;
        this.boardRenderer = boardRenderer;
        this.renderAssetLoader = renderAssetLoader;
    }

    @Override
    public void show() {
        cameraController.initialize();
        renderAssetLoader.queueAssets();
    }

    @Override
    public void render(float delta) {
        if (!assetsLoaded /*&& renderAssetLoader.update()*/) {
            boardRenderer.initializeSprites(renderAssetLoader.getAssets());
            assetsLoaded = true;
        }

        if (assetsLoaded) {
            renderAssetLoader.update();
            renderAssetLoader.getAssets().tileTextures().update();
            cameraController.update(delta);
            OrthographicCamera cam = cameraController.getCamera();
            boardRenderer.render(cam);
        } else {
            renderAssetLoader.renderLoading();
        }
    }

    @Override public void resize(int width, int height) { cameraController.resize(width, height); }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        boardRenderer.dispose();
        renderAssetLoader.dispose();
        cameraController.dispose();
    }
}

