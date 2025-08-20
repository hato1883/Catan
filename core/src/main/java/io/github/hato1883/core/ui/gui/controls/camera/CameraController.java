package io.github.hato1883.core.ui.gui.controls.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import io.github.hato1883.core.config.RendererConfig;

/**
 * Encapsulates camera logic & input; keeps Screen & Renderer clean. SRP/LoD.
 */

public final class CameraController {
    private final RendererConfig config;
    private OrthographicCamera camera;

    public CameraController(RendererConfig config) {
        this.config = config;
    }

    public void initialize() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = config.minZoom(); // 1.0f is the default
        camera.position.set(0, 0, 0);
        camera.update();
    }

    public void update(float delta) {
        float baseZoomSpeed = 1.5f;
        float zoomFactor = (float) Math.pow(baseZoomSpeed, Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            camera.zoom /= zoomFactor;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            camera.zoom *= zoomFactor;
        }
        camera.zoom = MathUtils.clamp(camera.zoom, config.minZoom(), config.maxZoom());
        camera.update();
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.position.set(0, 0, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() { return camera; }

    public void dispose() { /* nothing yet */ }
}
