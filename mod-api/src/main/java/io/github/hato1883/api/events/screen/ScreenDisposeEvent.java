package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;

public class ScreenDisposeEvent extends ScreenEvent {
    public ScreenDisposeEvent(ICameraScreen screen) { super(screen); }
}
