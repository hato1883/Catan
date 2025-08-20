package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.entities.player.IPlayer;

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
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
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

