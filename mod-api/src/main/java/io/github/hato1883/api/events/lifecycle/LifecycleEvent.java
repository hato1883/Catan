package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.game.IGameState;

import java.util.Optional;

/**
 * Root event for all Gameplay related events such as:
 *  * <li>Player trading</li>
 *  * <li>Player Rolling dice</li>
 *  * <li>Robber Moved</li>
 *  * <li>Player Build</li>
 *  */
public abstract class LifecycleEvent implements IEvent {
    /* add default fields if needed */

    LifecycleEvent(IGameState state) {}

    public Optional<IGameState> getState() {
        return Optional.empty();
    }
}
