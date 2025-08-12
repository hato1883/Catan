package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

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
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 *   <li>{@link PhaseEnterEvent}</li>
 *   <li>{@link PhaseExitEvent}</li>
 * </ul>
 */
public class PhaseUpdateEvent extends PhaseEvent {
    public PhaseUpdateEvent(IGameState gameState, IGamePhase phase) {
        super(gameState, phase);
    }
}
