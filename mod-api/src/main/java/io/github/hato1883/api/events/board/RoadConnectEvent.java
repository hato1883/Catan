package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IRoad;
import io.github.hato1883.game.event.EventBus;

/**
 * Event triggered when a road is connected to another road or structure on the board.
 * <p>
 * This event is fired after a successful road placement and
 * connection resolution.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(RoadConnectEvent.class, event -> {
 *     IRoad road = event.getRoad();
 *     System.out.println("Road connected: " + road);
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link StructureConnectEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class RoadConnectEvent extends StructureConnectEvent {

    /**
     * Creates a new road connection event.
     *
     * @param gameState the current game state
     * @param road the road that has been connected
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * RoadConnectEvent event = new RoadConnectEvent(gameState, road);
     * IRoad r = event.getRoad();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #getRoad()}</li>
     * </ul>
     */
    public RoadConnectEvent(IGameState gameState, IRoad road) {
        super(gameState, road);
    }

    /**
     * Gets the road that was connected.
     *
     * @return the road
     *
     * <h3>Defaults:</h3>
     * Returns the {@link IRoad} instance provided when this event was created.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IRoad connectedRoad = event.getRoad();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link IRoad}</li>
     * </ul>
     */
    public IRoad getRoad() {
        return (IRoad) getStructure();
    }
}

