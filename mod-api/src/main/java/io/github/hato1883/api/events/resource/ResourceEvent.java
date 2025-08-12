package io.github.hato1883.api.events.resource;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.resource.ResourceType;

import java.util.Map;

/**
 * Base class for all events related to resource interaction by a player.
 * Includes common data shared across resource gain, spend, discard, steal, and production events.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ResourceGainEvent.class, event -> {
 *     IPlayer player = event.getPlayer();
 *     Map<ResourceType, Integer> resources = event.getResources();
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ResourceGainEvent}</li>
 *   <li>{@link ResourceSpendEvent}</li>
 *   <li>{@link ResourceDiscardEvent}</li>
 *   <li>{@link ResourceStealEvent}</li>
 *   <li>{@link ResourceProductionEvent}</li>
 * </ul>
 */
public abstract class ResourceEvent extends GameEvent {

    protected final IPlayer player;
    protected final Map<ResourceType, Integer> resources;

    /**
     * Constructs a new ResourceEvent.
     *
     * @param gameState    the current game state
     * @param player       the player involved
     * @param resources    map of resources involved
     */
    protected ResourceEvent(IGameState gameState, IPlayer player, Map<ResourceType, Integer> resources) {
        super(gameState);
        this.player = player;
        this.resources = resources;
    }

    /**
     * @return the player involved in the event
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer player = event.getPlayer();
     * }</pre>
     */
    public IPlayer getPlayer() {
        return player;
    }

    /**
     * @return map of resources involved in the event
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * Map<ResourceType, Integer> resources = event.getResources();
     * }</pre>
     */
    public Map<ResourceType, Integer> getResources() {
        return resources;
    }
}

