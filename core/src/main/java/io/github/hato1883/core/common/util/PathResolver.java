package io.github.hato1883.core.common.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.hato1883.core.ui.gui.GameGUIMain.GAME_NAME;

public final class PathResolver {
    private PathResolver() {}

    public static Path getGameDataDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA"); // %AppData%/Roaming
            if (appData != null) {
                return Paths.get(appData, GAME_NAME);
            } else {
                return Paths.get(userHome, GAME_NAME); // fallback
            }
        } else if (os.contains("mac")) {
            return Paths.get(userHome, "Library", "Application Support", GAME_NAME);
        } else {
            // Assume Linux or Unix
            return Paths.get(userHome, ".local", "share", GAME_NAME);
        }
    }

}
