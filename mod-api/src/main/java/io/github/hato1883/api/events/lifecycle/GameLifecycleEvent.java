package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.game.IGameState;

/**
 * Base event class for core game lifecycle events.
 * Triggered during key game state transitions.
 * All core lifecycle events extend this class.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(GameStartEvent.class, event -> {
 *     System.out.println("Game started!");
 *     IGameState state = event.getGameState();
 *     // additional logic here
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link GameStartEvent}</li>
 *   <li>{@link GameEndEvent}</li>
 * </ul>
 */
public abstract class GameLifecycleEvent extends GameEvent {

    /**
     * Creates a new lifecycle event with the given game state.
     * @param gameState the current game state when this event is fired
     */
    public GameLifecycleEvent(IGameState gameState) {
        super(gameState);
    }
}
