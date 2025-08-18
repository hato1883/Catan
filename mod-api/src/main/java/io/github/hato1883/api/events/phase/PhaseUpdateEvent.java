package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IGameState;

/**
 * Event fired periodically while a game phase is active.
 * Can be used for ongoing updates or checks during the phase.
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PhaseUpdateEvent.class, event -> {
 *     // Called repeatedly during the phase
 *     IGamePhase phase = event.getPhase();
 *     // Possibly update UI or state based on phase progress
 * });
 * }</pre>
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PhaseEnterEvent}</li>
 *   <li>{@link PhaseExitEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class PhaseUpdateEvent extends PhaseEvent {
    public PhaseUpdateEvent(IGameState gameState, IGamePhase phase) {
        super(gameState, phase);
    }
}
