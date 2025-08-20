package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.entities.player.IPlayer;
import io.github.hato1883.api.events.player.PlayerEvent;

/**
 * Fired when a player joins the game.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerJoinEvent.class, event -> {
 *     IPlayer player = event.getPlayer();
 *     System.out.println("Welcome " + player.getName() + " to the game!");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class PlayerJoinEvent extends NetworkEvent implements Cancelable {

    private final IPlayer player;
    private boolean canceled = false;

    /**
     * Creates a new player join event.
     *
     * @param player    the player who joined
     */
    public PlayerJoinEvent(IPlayer player) {
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

