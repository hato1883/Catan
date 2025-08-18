package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameplayEvent;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.events.network.PlayerJoinEvent;
import io.github.hato1883.api.events.network.PlayerLeaveEvent;

/**
 * Base class for all events that involve a specific player in the game.
 * This class stores a reference to the game state and the player involved.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerJoinEvent.class, event -> {
 *     IPlayer player = event.getPlayer();
 *     System.out.println(player.getName() + " joined the game!");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerJoinEvent}</li>
 *     <li>{@link PlayerLeaveEvent}</li>
 *     <li>{@link PlayerEliminatedEvent}</li>
 *     <li>{@link PlayerResourceUpdateEvent}</li>
 * </ul>
 */
public abstract class PlayerEvent extends GameplayEvent {

    private final IPlayer player;

    /**
     * Constructs a new player event.
     *
     * @param gameState the current game state
     * @param player    the player involved in the event
     */
    protected PlayerEvent(IGameState gameState, IPlayer player) {
        super(gameState);
        this.player = player;
    }

    /**
     * Gets the player involved in this event.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer player = event.getPlayer();
     * System.out.println("Event triggered for player: " + player.getName());
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
