package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a player is eliminated from the game.
 * This may occur due to losing all resources, failing victory conditions,
 * or other game-specific elimination rules.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerEliminatedEvent.class, event -> {
 *     System.out.println("Player " + event.getPlayer().getName() + " has been eliminated.");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerEliminatedEvent extends PlayerEvent {

    /**
     * Creates a new player eliminated event.
     *
     * @param gameState the current game state
     * @param player    the player eliminated
     */
    public PlayerEliminatedEvent(IGameState gameState, IPlayer player) {
        super(gameState, player);
    }
}

