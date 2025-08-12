package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IStructure;
import io.github.hato1883.game.event.EventBus;

/**
 * Base event for all player structure placement events.
 * Fired whenever a player attempts to place a structure on the board.
 * This event provides common data about the structure and the current game state.
 * This event supports cancellation to prevent the structure from being built.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerBuildStructureEvent.class, event -> {
 *     IGameState gameState = event.getGameState();
 *     IStructure structure = event.getStructure();
 *     // Validate or modify event behavior here
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PlayerBuildBuildingEvent}</li>
 *   <li>{@link PlayerBuildRoadEvent}</li>
 *   <li>{@link PlayerBuildPortEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerBuildStructureEvent extends PlayerActionEvent {
    private final IStructure structure;

    /**
     * Constructs a new PlayerBuildStructureEvent.
     *
     * @param gameState the current game state
     * @param structure the structure being placed
     */
    public PlayerBuildStructureEvent(IGameState gameState, IStructure structure) {
        super(gameState, structure.getOwner());
        this.structure = structure;
    }

    /**
     * Gets the structure being placed by the player.
     *
     * @return the structure instance
     *
     * <h3>Defaults:</h3>
     * The structure represents the exact instance the player is attempting to place.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IStructure structure = event.getStructure();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link PlayerBuildBuildingEvent#getBuilding()}</li>
     *   <li>{@link PlayerBuildRoadEvent#getRoad()}</li>
     *   <li>{@link PlayerBuildPortEvent#getPort()}</li>
     * </ul>
     */
    public IStructure getStructure() {
        return structure;
    }
}
