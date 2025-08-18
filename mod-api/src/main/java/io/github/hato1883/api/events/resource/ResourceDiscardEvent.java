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
 * Event fired when a player discards resources (e.g., due to robber or card effects).
 * Fired after the player has chosen the resources to discard but before they are removed from the player's inventory.
 * This event is cancellable to prevent the discard from occurring.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ResourceDiscardEvent.class, event -> {
 *     IGameState state = event.getGameState();
 *     IResourceType resource = event.getResourceType();
 *     if (shouldCancelDiscard(resource)) {
 *         event.setCancelled(true);
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
public class ResourceDiscardEvent extends ResourceEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Creates a new ResourceDiscardEvent.
     *
     * @param gameState the current game state
     * @param discarded map of all discarded resources
     */
    public ResourceDiscardEvent(IGameState gameState, IPlayer player, Map<IResourceType, Integer> discarded) {
        super(gameState, player, discarded);
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

