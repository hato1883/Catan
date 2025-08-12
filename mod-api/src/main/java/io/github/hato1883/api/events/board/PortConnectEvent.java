package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IPort;
import io.github.hato1883.game.event.EventBus;

/**
 * Event triggered when a port is connected to a road or settlement.
 * <p>
 * This event is fired after a successful connection between a port and
 * a player's network.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PortConnectEvent.class, event -> {
 *     IPort port = event.getPort();
 *     System.out.println("Port connected: " + port);
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link StructureConnectEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PortConnectEvent extends StructureConnectEvent {

    /**
     * Creates a new port connection event.
     *
     * @param gameState the current game state
     * @param port the port that has been connected
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * PortConnectEvent event = new PortConnectEvent(gameState, port);
     * IPort p = event.getPort();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #getPort()}</li>
     * </ul>
     */
    public PortConnectEvent(IGameState gameState, IPort port) {
        super(gameState, port);
    }

    /**
     * Gets the port that was connected.
     *
     * @return the port
     *
     * <h3>Defaults:</h3>
     * Returns the {@link IPort} instance provided when this event was created.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPort connectedPort = event.getPort();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link IPort}</li>
     * </ul>
     */
    public IPort getPort() {
        return (IPort) getStructure();
    }
}

