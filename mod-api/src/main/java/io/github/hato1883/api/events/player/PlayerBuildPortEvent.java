package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IPort;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when a player attempts to build a port.
 * Extends {@link PlayerBuildStructureEvent} and supports cancellation.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerBuildPortEvent.class, event -> {
 *     IPort port = event.getPort();
 *     if (!canBuildPort(port)) {
 *         event.setCancelled(true);
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PlayerBuildStructureEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerBuildPortEvent extends PlayerBuildStructureEvent {

    /**
     * Constructs a new PlayerBuildPortEvent.
     *
     * @param gameState the current game state
     * @param port the port being constructed
     */
    public PlayerBuildPortEvent(IGameState gameState, IPort port) {
        super(gameState, port);
    }

    /**
     * Gets the port the player is attempting to build.
     *
     * @return the port instance
     *
     * <h3>Defaults:</h3>
     * Represents the port location and type the player wants to build.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPort port = event.getPort();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link PlayerBuildStructureEvent#getStructure()}</li>
     * </ul>
     */
    public IPort getPort() {
        return (IPort) super.getStructure();
    }
}

