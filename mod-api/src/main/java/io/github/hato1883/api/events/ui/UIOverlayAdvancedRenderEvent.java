package io.github.hato1883.api.events.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.hato1883.api.events.IEvent;

/**
 * Advanced UI overlay render event for mods that need isolated batch instances.
 * The batch is valid only for the duration of the event handler.
 */
public class UIOverlayAdvancedRenderEvent implements IEvent {
    private final Stage stage;
    private final Skin skin;
    private final Camera camera;

    public UIOverlayAdvancedRenderEvent(Stage stage, Skin skin, Camera camera) {
        this.stage = stage;
        this.skin = skin;
        this.camera = camera;
    }

    public Stage getStage() { return stage; }
    public Skin getSkin() { return skin; }
    public Camera getCamera() { return camera; }

    /**
     * Returns a new SpriteBatch instance for advanced use. Caller is responsible for disposing it.
     */
    public SpriteBatch createSpriteBatch() {
        SpriteBatch batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        return batch;
    }

    /**
     * Returns a new PolygonSpriteBatch instance for advanced use. Caller is responsible for disposing it.
     */
    public PolygonSpriteBatch createPolygonBatch() {
        PolygonSpriteBatch batch = new PolygonSpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        return batch;
    }

    /**
     * Returns a new ShapeRenderer instance for advanced use. Caller is responsible for disposing it.
     */
    public ShapeRenderer createShapeRenderer() {
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);
        return renderer;
    }
}

