package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.world.IGameState;

import java.util.Optional;

public abstract class LifecycleEvent implements IEvent {
    /* add default fields if needed */

    LifecycleEvent(IGameState state) {}

    public Optional<IGameState> getState() {
        return Optional.empty();
    }
}
