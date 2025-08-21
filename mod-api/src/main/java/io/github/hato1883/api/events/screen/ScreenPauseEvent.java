package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;

public class ScreenPauseEvent extends ScreenEvent {
    public ScreenPauseEvent(ICameraScreen screen) { super(screen); }
}
