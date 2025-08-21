package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;

public class ScreenResizeEvent extends ScreenEvent {
    private final int width;
    private final int height;

    public ScreenResizeEvent(ICameraScreen screen, int width, int height) {
        super(screen);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
