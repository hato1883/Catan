package io.github.hato1883.core.ui.tui.screen;

import io.github.hato1883.api.ui.TUIScreen;
import io.github.hato1883.core.ui.tui.TUIInput;

public abstract class BaseTUIScreen implements TUIScreen {

    // --- Final methods to guarantee lifecycle consistency ---
    @Override
    public final void show() {
        TUIInput.clear();
        onShow();  // hook for subclass
    }

    @Override
    public final void hide() {
        onHide(); // hook for subclass
    }

    // --- Hooks for subclasses ---
    protected void onShow() {
        // optional override
    }

    protected void onHide() {
        // optional override
    }

    // --- Subclass responsibilities ---
    @Override
    public abstract void render();

    @Override
    public abstract boolean isFinished();
}
