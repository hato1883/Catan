package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.world.board.IPort;

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
 *     <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
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

