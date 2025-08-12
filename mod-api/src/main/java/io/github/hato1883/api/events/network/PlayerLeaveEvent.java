package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;
import io.github.hato1883.api.events.player.PlayerEvent;

/**
 * Fired when a player leaves the game voluntarily or due to disconnection.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerLeaveEvent.class, event -> {
 *     System.out.println(event.getPlayer().getName() + " has left the game.");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerLeaveEvent extends NetworkEvent {

    private final IPlayer player;

    /**
     * Creates a new player leave event.
     *
     * @param gameState the current game state
     * @param player    the player who left
     */
    public PlayerLeaveEvent(IGameState gameState, IPlayer player) {
        super(gameState);
        this.player = player;
    }

    /**
     * Gets the player who joined the game.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer player = event.getPlayer();
     * System.out.println(player.getName() + " has joined the game");
     * }</pre>
     *
     * @return the player
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link IPlayer}</li>
     * </ul>
     */
    public IPlayer getPlayer() {
        return player;
    }
}

