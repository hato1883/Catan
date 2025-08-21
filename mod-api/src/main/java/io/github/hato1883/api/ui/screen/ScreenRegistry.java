package io.github.hato1883.api.ui.screen;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.screen.ScreenLifecycleListener;

import java.util.Set;

/**
 * API for registering and switching between screens in the Catan modding framework.
 * Mods can use this to add their own screens and switch to them by unique ID.
 */
public interface ScreenRegistry {
    /**
     * Registers a new screen with the given unique ID. If a screen with this ID already exists, it will be replaced.
     * @param id Unique identifier for the screen (e.g., "modid:settings").
     * @param screen The screen instance to register.
     */
    void registerScreen(Identifier id, ICameraScreen screen);

    /**
     * Switches to the screen with the given ID, if it is registered.
     * @param id The unique screen ID.
     * @return true if the screen was found and switched to, false otherwise.
     */
    boolean switchToScreen(Identifier id);

    /**
     * Gets the registered screen for the given ID, or null if not found.
     * @param id The unique screen ID.
     * @return The screen instance, or null.
     */
    ICameraScreen getScreen(Identifier id);

    /**
     * Returns all registered screen IDs.
     */
    Set<Identifier> getRegisteredScreenIds();

    /**
     * Registers a ScreenLifecycleListener for a specific screen identified by the given Identifier.
     * Only events for that screen will be delivered to this listener.
     * @param screenId The unique Identifier for the screen.
     * @param listener The listener to register.
     */
    void registerScreenLifecycleListener(Identifier screenId, ScreenLifecycleListener listener);

    /**
     * Unregisters a ScreenLifecycleListener for a specific screen.
     * @param screenId The unique Identifier for the screen.
     * @param listener The listener to unregister.
     */
    void unregisterScreenLifecycleListener(Identifier screenId, ScreenLifecycleListener listener);
}
