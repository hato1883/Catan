package io.github.hato1883.api.ui.screen;

import io.github.hato1883.api.Identifier;

/**
 * API for managing and navigating between screens in the Catan modding framework.
 * Mods can use this to switch to their own or other screens by unique Identifier.
 */
public interface IScreenManager {
    /**
     * Sets the current screen to the one registered with the given Identifier.
     * @param id The unique Identifier for the screen.
     * @return true if the screen was found and set, false otherwise.
     */
    boolean setScreen(Identifier id);

    /**
     * Pushes a new screen onto the screen stack and makes it the current screen.
     * @param id The unique Identifier for the screen.
     * @return true if the screen was found and pushed, false otherwise.
     */
    boolean pushScreen(Identifier id);

    /**
     * Pops the current screen from the stack and returns to the previous screen.
     * If there is no previous screen, switches to the default screen.
     */
    void popScreen();

    /**
     * Gets the Identifier of the current screen.
     */
    Identifier getCurrentScreenId();

    /**
     * Gets the Identifier of the default screen.
     */
    Identifier getDefaultScreenId();

    /**
     * Sets the default screen to the one registered with the given Identifier.
     * @param id The unique Identifier for the screen.
     */
    void setDefaultScreen(Identifier id);

    /**
     * sets the current screen to the default screen.
     * If the default screen is not set, it will do nothing.
     */
    void showDefaultScreen();
}

