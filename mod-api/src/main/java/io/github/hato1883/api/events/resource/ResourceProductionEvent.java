package io.github.hato1883.api.events.resource;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.entities.player.IPlayer;
import io.github.hato1883.api.entities.resource.IResourceType;

import java.util.Map;

/**
 * Fired when a player produces resources, typically due to a dice roll or tile activation.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ResourceProductionEvent.class, event -> {
 *     System.out.println(event.getPlayer().getName() + " produced " +
 *         event.getAmount() + " " + event.getResourceType());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ResourceEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class ResourceProductionEvent extends ResourceEvent {
    public ResourceProductionEvent(IGameState gameState, IPlayer player, Map<IResourceType, Integer> resources) {
        super(gameState, player, resources);
    }
}

