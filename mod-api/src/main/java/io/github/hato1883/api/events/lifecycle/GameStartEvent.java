package io.github.hato1883.api.events.lifecycle;


import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a game session starts, after world creation and setup is complete.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(GameStartEvent.class, event -> {
 *     System.out.println("Game has started!");
 *     IGameState state = event.getGameState();
 *     // Initialize player data or setup game UI here
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link GameLifecycleEvent}</li>
 *   <li>{@link GameEndEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class GameStartEvent extends GameLifecycleEvent implements Cancelable {
    private boolean canceled = false;

    public GameStartEvent(IGameState gameState) {
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
