package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;

public class ScreenShowEvent extends ScreenEvent {
    public ScreenShowEvent(ICameraScreen screen) { super(screen); }
}
