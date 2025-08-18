package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IGameState;

/**
 * Event fired when a game phase is entered.
 * Triggered at the start of the phase.
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PhaseEnterEvent.class, event -> {
 *     System.out.println("Phase entered: " + event.getPhase().getName());
 * });
 * }</pre>
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PhaseUpdateEvent}</li>
 *   <li>{@link PhaseExitEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class PhaseEnterEvent extends PhaseEvent implements Cancelable {

    private boolean canceled = false;

    public PhaseEnterEvent(IGameState gameState, IGamePhase phase) {
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
