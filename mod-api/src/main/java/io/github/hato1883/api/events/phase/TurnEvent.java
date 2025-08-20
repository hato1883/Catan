package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.GameplayEvent;
import io.github.hato1883.api.world.IGameState;

/**
 * Base class for turn-related events.
 * These events are fired at the start and end of a player's turn.
 * <p>
 * The game state can be accessed to inspect the current game context.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(TurnEvent.class, event -> {
 *     IGameState state = event.getGameState();
 *     System.out.println("Turn event for player: " + state.getCurrentPlayer().getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link TurnStartEvent}</li>
 *   <li>{@link TurnEndEvent}</li>
 *   <li>{@link GameplayEvent}</li>
 * </ul>
 */
public abstract class TurnEvent extends GameplayEvent {

    /**
     * Constructs a TurnEvent with the current game state.
     *
     * @param gameState the current state of the game
     */
    public TurnEvent(IGameState gameState) {
        super(gameState);
    }
}
