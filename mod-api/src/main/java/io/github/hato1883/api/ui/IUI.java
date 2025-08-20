package io.github.hato1883.api.ui;

/**
 * Common interface for all game UIs.
 * Can be implemented by GUI (LibGDX) or TUI.
 */
public interface IUI {
    /**
     * Entry point to start the UI.
     * GUI will launch screens, TUI will start loop.
     */
    void create();
}
