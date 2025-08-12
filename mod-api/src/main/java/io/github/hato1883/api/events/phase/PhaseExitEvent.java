package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when a game phase is exited.
 * Triggered at the end of the phase.
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PhaseExitEvent.class, event -> {
 *     System.out.println("Phase exited: " + event.getPhase().getName());
 * });
 * }</pre>
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 *   <li>{@link PhaseEnterEvent}</li>
 *   <li>{@link PhaseUpdateEvent}</li>
 * </ul>
 */
public class PhaseExitEvent extends PhaseEvent implements Cancelable {

    private boolean canceled = false;

    public PhaseExitEvent(IGameState gameState, IGamePhase phase) {
        super(gameState, phase);
    }

    /**
     * Checks if this event has been canceled.
     *
     * @return {@code true} if the event has been canceled, {@code false} otherwise.
     */
    @Override
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Cancels this event.
     * <p>
     * Once an event is canceled, it should not be uncanceled.
     * Multiple calls to this method have no additional effect.
     * </p>
     */
    @Override
    public void cancel() {
        canceled = true;
    }
}
