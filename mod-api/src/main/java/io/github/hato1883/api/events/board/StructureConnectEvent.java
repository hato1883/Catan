package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IStructure;

/**
 * Represents a generic structure connection event.
 * <p>
 * This event is triggered when any type of structure (road, port, etc.)
 * becomes connected to another part of the game board.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(StructureConnectEvent.class, event -> {
 *     IStructure structure = event.getStructure();
 *     System.out.println("Structure connected: " + structure);
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link RoadConnectEvent}</li>
 *     <li>{@link PortConnectEvent}</li>
 * </ul>
 */
public abstract class StructureConnectEvent extends BoardEvent implements Cancelable {

    private final IStructure structure;
    private boolean canceled = false;

    /**
     * Creates a new structure connection event.
     *
     * @param gameState the current game state
     * @param structure the structure being connected
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * StructureConnectEvent event = new RoadConnectEvent(gameState, road);
     * IStructure s = event.getStructure();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link #getStructure()}</li>
     * </ul>
     */
    protected StructureConnectEvent(IGameState gameState, IStructure structure) {
        super(gameState);
        this.structure = structure;
    }

    /**
     * Gets the structure that is being connected.
     *
     * @return the structure
     *
     * <h3>Defaults:</h3>
     * Returns the exact structure instance provided at construction time.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IStructure connected = event.getStructure();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link IStructure}</li>
     * </ul>
     */
    public IStructure getStructure() {
        return structure;
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

