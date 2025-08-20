package io.github.hato1883.api.events.board;

import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.world.board.IStructure;

/**
 * Represents a generic event related to a structure in the game.
 * <p>
 * This is the base class for more specific structure-related events, such as
 * upgrades or destruction.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(StructureUpgradeEvent.class, event -> {
 *     IStructure structure = event.getStructure();
 *     System.out.println("Structure affected: " + structure);
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link StructureUpgradeEvent}</li>
 *     <li>{@link StructureDestroyEvent}</li>
 * </ul>
 */
public abstract class StructureEvent extends BoardEvent {

    private final IStructure structure;

    /**
     * Constructs a new StructureEvent.
     *
     * @param gameState the current game state
     * @param structure the structure affected by the event
     */
    public StructureEvent(IGameState gameState, IStructure structure) {
        super(gameState);
        this.structure = structure;
    }

    /**
     * Gets the structure affected by this event.
     *
     * @return the structure involved
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IStructure structure = event.getStructure();
     * // Perform logic on the structure
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
}

