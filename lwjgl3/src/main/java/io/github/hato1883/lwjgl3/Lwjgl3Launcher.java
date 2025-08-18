package io.github.hato1883.lwjgl3;

import io.github.hato1883.api.ui.IUI;
import io.github.hato1883.core.GameBootstrapper;
import io.github.hato1883.core.ui.UIFactory;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;

        boolean useGui = true; // configurable

        IUI ui = UIFactory.createUI(useGui);

        // Allow mods to load
        new GameBootstrapper(ui).start();
    }
}
