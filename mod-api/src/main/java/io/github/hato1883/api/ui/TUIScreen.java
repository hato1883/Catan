package io.github.hato1883.api.ui;

public interface TUIScreen {
    void show();       // called when the screen is set
    void render();     // called repeatedly in a loop (non-blocking)
    void hide();       // called when switching away
    boolean isFinished(); // signals when the user wants to move to the next screen
}
