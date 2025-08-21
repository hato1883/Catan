package io.github.hato1883.api.ui.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.screen.*;
import io.github.hato1883.api.Events;

/**
 * Abstract base class for event-driven screens. Handles all event dispatching and boilerplate for modders.
 * Extend this class for your custom screen to automatically participate in the screen event system.
 * Override {@code exposeEvents()} to return false if you want a private screen (no event dispatch).
 */
public abstract class AbstractEventDrivenScreen implements ICameraScreen {
    private final Identifier screenId;
    protected final OrthographicCamera camera;

    public AbstractEventDrivenScreen(Identifier screenId) {
        this.screenId = screenId;
        this.camera = new OrthographicCamera();
    }

    @Override
    public String getScreenId() { return screenId.toString(); }

    @Override
    public OrthographicCamera getCamera() { return camera; }

    // --- LibGDX Screen lifecycle ---
    @Override
    public final void show() {
        onShow();
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenShowEvent(this));
    }
    @Override
    public final void render(float delta) {
        onRender(delta);
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenRenderEvent(this));
    }
    @Override
    public final void resize(int width, int height) {
        onResize(width, height);
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenResizeEvent(this, width, height));
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
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenHideEvent(this));
    }
    @Override
    public final void dispose() {
        onDispose();
        if (exposeEvents()) Events.bus().dispatchOnMainThread(new ScreenDisposeEvent(this));
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

