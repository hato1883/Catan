package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.game.event.EventBus;

import java.util.Map;

/**
 * Fired when a player's resource counts are updated.
 * This can happen due to production, spending, trading, or stealing.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerResourceUpdateEvent.class, event -> {
 *     Map<IResourceType, Integer> newResources = event.getUpdatedResources();
 *     System.out.println(event.getPlayer().getName() + " now has: " + newResources);
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PlayerEvent}</li>
 *     <li>{@link IResourceType}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerResourceUpdateEvent extends PlayerEvent {

    private final Map<IResourceType, Integer> updatedResources;

    /**
     * Creates a new player resource update event.
     *
     * @param gameState       the current game state
     * @param player          the player whose resources changed
     * @param updatedResources a map of updated resource counts
     */
    public PlayerResourceUpdateEvent(IGameState gameState, IPlayer player, Map<IResourceType, Integer> updatedResources) {
        super(gameState, player);
        this.updatedResources = updatedResources;
    }

    /**
     * Gets the updated resource counts for the player.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * Map<IResourceType, Integer> resources = event.getUpdatedResources();
     * }</pre>
     *
     * @return a map of resource types to counts
     *
     * <h3>See Also:</h3>
     * <ul>
     *     <li>{@link IResourceType}</li>
     * </ul>
     */
    public Map<IResourceType, Integer> getUpdatedResources() {
        return updatedResources;
    }
}

