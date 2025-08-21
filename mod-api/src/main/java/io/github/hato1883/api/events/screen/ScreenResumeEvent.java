package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;

public class ScreenResumeEvent extends ScreenEvent {
    public ScreenResumeEvent(ICameraScreen screen) { super(screen); }
}
