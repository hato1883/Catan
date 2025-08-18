package io.github.hato1883.api.events.resource;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.IResourceType;

import java.util.Map;

/**
 * Fired when a player spends resources (e.g., for building or trading).
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ResourceSpendEvent.class, event -> {
 *     if (event.getResourceType().getId().equals("brick") && event.getAmount() > 3) {
 *         System.out.println("Heavy brick usage detected!");
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ResourceEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class ResourceSpendEvent extends ResourceEvent implements Cancelable {

    private boolean canceled = false;

    public ResourceSpendEvent(IGameState gameState, IPlayer player, Map<IResourceType, Integer> resources) {
        super(gameState, player, resources);
    }

    /**
     * Checks if this event has been canceled.
     *
     * @return {@code true} if the event has been canceled, {@code false} otherwise.
     */
    @Override
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Cancels this event.
     * <p>
     * Once an event is canceled, it should not be uncanceled.
     * Multiple calls to this method have no additional effect.
     * </p>
     */
    @Override
    public void cancel() {
        canceled = true;
    }
}

