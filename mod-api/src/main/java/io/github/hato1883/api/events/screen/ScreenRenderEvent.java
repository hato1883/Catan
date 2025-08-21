package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;

public class ScreenRenderEvent extends ScreenEvent {
    public ScreenRenderEvent(ICameraScreen screen) { super(screen); }
}
