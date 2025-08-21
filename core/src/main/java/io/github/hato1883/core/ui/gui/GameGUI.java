package io.github.hato1883.core.ui.gui;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.hato1883.api.ui.IUI;

public class GameGUI implements IUI {

    @Override
    public void create() {
        Lwjgl3ApplicationConfiguration config = getDefaultConfiguration();
        new Lwjgl3Application(new GameGUIMain(), config);
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Catan");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(640, 480);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        // Antialiasing and back buffer configuration
        configuration.setBackBufferConfig(
            8, 8, 8, 8, // RGBA bits
            16,         // depth buffer bits
            0,          // stencil buffer bits
            8           // samples (anti-aliasing)
        );
        return configuration;
    }
}


