package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.board.IPort;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when a player uses a port for trade.
 * This event supports cancellation to prevent the port usage.
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link IPort ()}}</li>
 *   <li>{@link PlayerTradeEvent ()}</li>
 *   <li>{@link PlayerTradeAcceptEvent ()}}</li>
 *   <li>{@link PlayerTradeCancelEvent ()}}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerUsePortEvent extends PlayerActionEvent {
    private final IPort port;

    /**
     * Constructs a new PlayerUsePortEvent.
     *
     * @param gameState The current game state
     * @param player The player using the port
     * @param port The port being used
     */
    public PlayerUsePortEvent(IGameState gameState, IPlayer player, IPort port) {
        super(gameState, player);
        this.port = port;
    }

    /**
     * Returns the port involved in this event.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPort port = event.getPort();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link PlayerActionEvent#getPlayer()}</li>
     * </ul>
     *
     * @return The port being used
     */
    public IPort getPort() {
        return port;
    }
}
