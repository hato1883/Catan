package io.github.hato1883.api.ui.screen;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Services;

/**
 * Facade for accessing and using the IScreenManager service.
 * Provides convenient static methods for common screen management actions.
 */
public final class Screens {
    private static IScreenManager instance;
    private Screens() {}

    /**
     * Initializes the Screens facade with a custom IScreenManager instance.
     * This is intended for testing or advanced use only. Throws if already initialized.
     */
    public static synchronized void initialize(IScreenManager mgr) {
        if (instance != null) throw new IllegalStateException("Screens already initialized");
        instance = mgr;
    }

    /**
     * Resets the Screens facade to use the default service locator instance.
     * This is intended for testing or advanced use only.
     */
    static synchronized void reset() {
        instance = null;
    }

    /**
     * Returns the IScreenManager instance, using the service locator unless overridden by initialize().
     */
    static IScreenManager get() {
        if (instance != null) return instance;
        return instance = Services.require(IScreenManager.class);
    }

    /**
     * Sets the current screen by identifier.
     */
    public static boolean set(Identifier id) {
        return get().setScreen(id);
    }

    /**
     * Pushes a new screen onto the stack by identifier.
     */
    public static boolean push(Identifier id) {
        return get().pushScreen(id);
    }

    /**
     * Pops the current screen from the stack.
     */
    public static void pop() {
        get().popScreen();
    }

    /**
     * Gets the current screen identifier.
     */
    public static Identifier current() {
        return get().getCurrentScreenId();
    }

    /**
     * Gets the default screen identifier.
     */
    public static Identifier defaultScreen() {
        return get().getDefaultScreenId();
    }

    /**
     * Sets the default screen identifier.
     */
    public static void setDefault(Identifier id) {
        get().setDefaultScreen(id);
    }
}
