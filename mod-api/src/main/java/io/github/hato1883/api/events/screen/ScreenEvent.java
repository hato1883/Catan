package io.github.hato1883.api.events.screen;

import io.github.hato1883.api.ui.screen.ICameraScreen;
import io.github.hato1883.api.events.IEvent;

public abstract class ScreenEvent implements IEvent {
    private final ICameraScreen screen;
    public ScreenEvent(ICameraScreen screen) { this.screen = screen; }
    public ICameraScreen getScreen() { return screen; }
}
