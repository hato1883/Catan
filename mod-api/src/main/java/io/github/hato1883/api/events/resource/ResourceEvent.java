package io.github.hato1883.api.events.resource;

import io.github.hato1883.api.events.player.PlayerEvent;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.entities.player.IPlayer;
import io.github.hato1883.api.entities.resource.IResourceType;

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
public abstract class ResourceEvent extends PlayerEvent {

    private final Map<IResourceType, Integer> resources;

    public ResourceEvent(IGameState state, IPlayer player, Map<IResourceType, Integer> resources) {
        super(state, player);
        this.resources = resources;
    }

    public Map<IResourceType, Integer> getResources() {
        return resources;
    }
}

