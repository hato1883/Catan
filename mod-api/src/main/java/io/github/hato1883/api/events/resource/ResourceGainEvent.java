package io.github.hato1883.api.events.resource;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;
import io.github.hato1883.game.resource.ResourceType;

import java.util.Map;

/**
 * Fired when a player gains resources through any means (production, trade, etc).
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ResourceGainEvent.class, event -> {
 *     // Grant a bonus wood if gaining exactly 2 wood
 *     if (event.getResourceType().getId().equals("wood") && event.getAmount() == 2) {
 *         // custom bonus logic
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ResourceEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class ResourceGainEvent extends ResourceEvent {
    public ResourceGainEvent(IGameState gameState, IPlayer player, Map<ResourceType, Integer> resources) {
        super(gameState, player, resources);
    }
}

