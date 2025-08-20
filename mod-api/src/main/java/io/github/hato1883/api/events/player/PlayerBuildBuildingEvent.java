package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.world.board.IBuilding;

/**
 * Event triggered when a player attempts to build a building.
 * Extends {@link PlayerBuildStructureEvent} to provide building-specific context.
 * This event supports cancellation to prevent the building from being placed.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerBuildBuildingEvent.class, event -> {
 *     IBuilding building = event.getBuilding();
 *     if (!someCustomValidation(building)) {
 *         event.setCancelled(true);
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PlayerBuildStructureEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class PlayerBuildBuildingEvent extends PlayerBuildStructureEvent {

    /**
     * Constructs a new PlayerBuildBuildingEvent.
     *
     * @param gameState the current game state
     * @param building the building being constructed
     */
    public PlayerBuildBuildingEvent(IGameState gameState, IBuilding building) {
        super(gameState, building);
    }

    /**
     * Gets the building the player is attempting to construct.
     *
     * @return the building instance
     *
     * <h3>Defaults:</h3>
     * Represents the building type and location the player wants to build.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IBuilding building = event.getBuilding();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link PlayerBuildStructureEvent#getStructure()}</li>
     * </ul>
     */
    public IBuilding getBuilding() {
        return (IBuilding) super.getStructure();
    }
}
