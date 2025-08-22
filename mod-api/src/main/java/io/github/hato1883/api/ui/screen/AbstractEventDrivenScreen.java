package io.github.hato1883.api.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.screen.*;
import io.github.hato1883.api.events.ui.*;
import io.github.hato1883.api.Events;
import io.github.hato1883.api.Registries;

import java.util.EnumMap;
import java.util.Map;

/**
 * Abstract base class for event-driven screens. Handles all event dispatching and boilerplate for modders.
 * Extend this class for your custom screen to automatically participate in the screen event system.
 * Override {@code exposeEvents()} to return false if you want a private screen (no event dispatch).
 */
public abstract class AbstractEventDrivenScreen implements ICameraScreen {
    private final Identifier screenId;
    protected final OrthographicCamera camera;
    private Stage overlayStage;
    private Skin overlaySkin;
    private boolean overlayInitialized = false;
    private final Map<BatchType, Object> uiBatches = new EnumMap<>(BatchType.class);
    private final Map<IBatchingJob, Object> isolatedBatches = new java.util.HashMap<>();

    public AbstractEventDrivenScreen(Identifier screenId) {
        this.screenId = screenId;
        this.camera = new OrthographicCamera();

        // Camera initialization for correct viewport and position
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Center camera on board
        camera.position.set(0, 0, 0);
        // centerCameraOnBoard();
        camera.zoom = 1.0f;
        camera.update();
    }

    @Override
    public String getScreenId() { return screenId.toString(); }

    @Override
    public OrthographicCamera getCamera() { return camera; }

    // --- Overlay UI setup ---
    private void ensureOverlayInitialized() {
        if (!overlayInitialized && exposeEvents()) {
            overlayStage = new Stage(); // Use default camera for UI, do not share main camera
            overlaySkin = createOverlaySkin();
            overlayInitialized = true;
        }
    }

    /**
     * Override to provide a custom Skin for the overlay. By default, loads "uiskin.json" from assets/ui.
     */
    protected Skin createOverlaySkin() {
        // Default: load from assets/ui/uiskin.json
        return new Skin(Gdx.files.internal("ui/uiskin.json"));
    }

    /**
     * Returns the overlay Stage for UIOverlayRenderEvent. Null if overlays are not enabled.
     */
    protected Stage getOverlayStage() {
        ensureOverlayInitialized();
        return overlayStage;
    }

    /**
     * Returns the overlay Skin for UIOverlayRenderEvent. Null if overlays are not enabled.
     */
    protected Skin getOverlaySkin() {
        ensureOverlayInitialized();
        return overlaySkin;
    }

    // --- LibGDX Screen lifecycle ---
    @Override
    public final void show() {
        onShow();
        ensureOverlayInitialized();
        ensureUIBatchesInitialized();
        ensureIsolatedBatchesInitialized();
        if (overlayStage != null) {
            Gdx.input.setInputProcessor(overlayStage);
        }
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenShowEvent(this));
    }

    private void ensureUIBatchesInitialized() {
        if (!uiBatches.isEmpty()) return;
        // By default, create all batch types. Subclasses can override this method for custom logic.
        for (BatchType type : BatchType.values()) {
            switch (type) {
                case SPRITE:
                    uiBatches.put(type, new SpriteBatch());
                    break;
                case POLYGON:
                    uiBatches.put(type, new PolygonSpriteBatch());
                    break;
                case SHAPE:
                    uiBatches.put(type, new ShapeRenderer());
                    break;
            }
        }
    }

    private void ensureIsolatedBatchesInitialized() {
        isolatedBatches.clear();
        for (IBatchingJob job : Registries.uiBatchingJobs().getAll()) {
            if (job instanceof IIsolatedBatchingJob) {
                BatchType type = job.getBatchType();
                Object batch = null;
                switch (type) {
                    case SPRITE:
                        batch = new SpriteBatch();
                        ((SpriteBatch)batch).setProjectionMatrix(camera.combined);
                        break;
                    case POLYGON:
                        batch = new PolygonSpriteBatch();
                        ((PolygonSpriteBatch)batch).setProjectionMatrix(camera.combined);
                        break;
                    case SHAPE:
                        batch = new ShapeRenderer();
                        ((ShapeRenderer)batch).setProjectionMatrix(camera.combined);
                        break;
                }
                if (batch != null) {
                    isolatedBatches.put(job, batch);
                }
            }
        }
    }

    @Override
    public final void render(float delta) {
        onRender(delta);
        if (exposeEvents()) {
            Events.bus().dispatchOnMainThread(new ScreenRenderEvent(this));
            Stage stage = getOverlayStage();
            Skin skin = getOverlaySkin();
            if (stage != null && skin != null) {
                Events.bus().dispatchOnMainThread(new UIOverlayRenderEvent(stage, skin));
                stage.act(delta);
                stage.draw();
            }
            // Fire UIBatchingRenderEvent for each batch type (shared batch)
            for (Map.Entry<BatchType, Object> entry : uiBatches.entrySet()) {
                Events.bus().dispatchOnMainThread(new UIBatchingRenderEvent(entry.getKey(), entry.getValue(), camera));
            }
            // Call all registered IBatchingJobs
            for (IBatchingJob job : Registries.uiBatchingJobs().getAll()) {
                BatchType type = job.getBatchType();
                Object batch;
                if (job instanceof IIsolatedBatchingJob) {
                    batch = isolatedBatches.get(job);
                } else {
                    batch = uiBatches.get(type);
                }
                if (batch != null) {

                    job.render(new BatchingContext(batch, camera));
                }
            }
        }
    }
    @Override
    public final void resize(int width, int height) {
        onResize(width, height);
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenResizeEvent(this, width, height));
        if (overlayStage != null) {
            overlayStage.getViewport().update(width, height, true);
        }
    }
    @Override
    public final void pause() {
        onPause();
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenPauseEvent(this));
    }
    @Override
    public final void resume() {
        onResume();
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenResumeEvent(this));
    }
    @Override
    public final void hide() {
        onHide();
        if (Gdx.input.getInputProcessor() == overlayStage) {
            Gdx.input.setInputProcessor(null);
        }
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenHideEvent(this));
    }
    @Override
    public final void dispose() {
        onDispose();
        if (Gdx.input.getInputProcessor() == overlayStage) {
            Gdx.input.setInputProcessor(null);
        }
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenDisposeEvent(this));
        if (overlayStage != null) overlayStage.dispose();
        if (overlaySkin != null) overlaySkin.dispose();
        // Dispose all UI batches
        for (Object batch : uiBatches.values()) {
            if (batch instanceof SpriteBatch) ((SpriteBatch) batch).dispose();
            else if (batch instanceof PolygonSpriteBatch) ((PolygonSpriteBatch) batch).dispose();
            else if (batch instanceof ShapeRenderer) ((ShapeRenderer) batch).dispose();
        }
        // Dispose all isolated batches
        for (Object batch : isolatedBatches.values()) {
            if (batch instanceof SpriteBatch) ((SpriteBatch) batch).dispose();
            else if (batch instanceof PolygonSpriteBatch) ((PolygonSpriteBatch) batch).dispose();
            else if (batch instanceof ShapeRenderer) ((ShapeRenderer) batch).dispose();
        }
        uiBatches.clear();
        isolatedBatches.clear();
        overlayStage = null;
        overlaySkin = null;
        overlayInitialized = false;
    }

    // --- Subclass hooks ---
    abstract protected void onShow();
    abstract protected void onRender(float delta);
    protected void onResize(int width, int height) {}
    protected void onPause() {}
    protected void onResume() {}
    protected void onHide() {}
    abstract protected void onDispose();

    /**
     * Override to return false if you want a private screen (no event dispatch).
     */
    protected boolean exposeEvents() { return true; }
}
