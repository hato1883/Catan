package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;

/**
 * Event fired at the start of a player's turn.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(TurnStartEvent.class, event -> {
 *     System.out.println("Turn started for player: " + event.getGameState().getCurrentPlayer().getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link TurnEndEvent}</li>
 *   <li>{@link TurnEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class TurnStartEvent extends TurnEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Constructs a TurnStartEvent with the current game state.
     *
     * @param gameState the current state of the game
     */
    public TurnStartEvent(IGameState gameState) {
        super(gameState);
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
