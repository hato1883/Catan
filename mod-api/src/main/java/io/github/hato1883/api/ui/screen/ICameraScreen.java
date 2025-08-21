package io.github.hato1883.api.ui.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * A screen that provides access to its camera.
 */
public interface ICameraScreen extends Screen {
    /**
     * Returns the unique identifier for this screen. Used for registration and filtering.
     */
    String getScreenId();

    /**
     * Returns the camera used by this screen.
     */
    OrthographicCamera getCamera();
}
