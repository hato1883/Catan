package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IRoad;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when a player attempts to build a road.
 * Extends {@link PlayerBuildStructureEvent} and supports cancellation.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerBuildRoadEvent.class, event -> {
 *     IRoad road = event.getRoad();
 *     if (someConditionFails(road)) {
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
public class PlayerBuildRoadEvent extends PlayerBuildStructureEvent {

    /**
     * Constructs a new PlayerBuildRoadEvent.
     *
     * @param gameState the current game state
     * @param road the road being constructed
     */
    public PlayerBuildRoadEvent(IGameState gameState, IRoad road) {
        super(gameState, road);
    }

    /**
     * Gets the road the player is attempting to build.
     *
     * @return the road instance
     *
     * <h3>Defaults:</h3>
     * Represents the road segment the player intends to place.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IRoad road = event.getRoad();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link PlayerBuildStructureEvent#getStructure()}</li>
     * </ul>
     */
    public IRoad getRoad() {
        return (IRoad) super.getStructure();
    }
}

