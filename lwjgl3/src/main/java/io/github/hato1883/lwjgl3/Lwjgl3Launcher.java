package io.github.hato1883.lwjgl3;

import io.github.hato1883.api.ui.IUI;
import io.github.hato1883.core.bootstrap.GameBootstrapper;
import io.github.hato1883.api.mod.load.ModLoadingException;
import io.github.hato1883.core.ui.UIFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;

        boolean useGui = true; // configurable

        IUI ui = UIFactory.createUI(useGui);

        // Allow mods to load
        try {
            new GameBootstrapper(ui).start();
        } catch (ModLoadingException ex) {
            Logger logger = LoggerFactory.getLogger(Lwjgl3Launcher.class);
            logger.error("Critical error during mod loading: {}", ex.getMessage(), ex);
            System.exit(1);
        }
    }
}
