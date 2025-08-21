package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;

public class ScreenHideEvent extends ScreenEvent {
    public ScreenHideEvent(ICameraScreen screen) { super(screen); }
}
