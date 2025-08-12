package io.github.hato1883.api.events.resource;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;
import io.github.hato1883.game.resource.ResourceType;

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
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class ResourceProductionEvent extends ResourceEvent {
    public ResourceProductionEvent(IGameState gameState, IPlayer player, Map<ResourceType, Integer> resources) {
        super(gameState, player, resources);
    }
}

