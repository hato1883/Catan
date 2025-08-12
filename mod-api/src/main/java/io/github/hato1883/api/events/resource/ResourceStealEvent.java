package io.github.hato1883.api.events.resource;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;
import io.github.hato1883.game.resource.ResourceType;

import java.util.Map;

/**
 * Fired when one player steals a resource from another.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ResourceStealEvent.class, event -> {
 *     System.out.println(event.getThief().getName() + " stole " +
 *         event.getAmount() + " " + event.getResourceType() +
 *         " from " + event.getVictim().getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ResourceEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class ResourceStealEvent extends ResourceEvent implements Cancelable {
    private final IPlayer victim;
    private boolean canceled = false;

    public ResourceStealEvent(IGameState gameState, IPlayer thief, IPlayer victim, Map<ResourceType, Integer> resources) {
        super(gameState, thief, resources);
        this.victim = victim;
    }


    /**
     * @deprecated This method is ambiguous in the context of a trade.
     * Use {@link #getThief()} or {@link #getVictim()} instead.
     * Calling this method will throw an exception.
     * @return Nothing, throws exception.
     * @throws UnsupportedOperationException use {@link #getThief()} or {@link #getVictim()} instead.
     */
    @Override
    @Deprecated
    public IPlayer getPlayer(){
        throw new UnsupportedOperationException(
            "ResourceStealEvent does not support getPlayer(). Use getThief() or getVictim() instead."
        );
    }

    /**
     * @return the player who stole the resource
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer thief = event.getThief();
     * }</pre>
     */
    public IPlayer getThief() {
        return super.getPlayer();
    }

    /**
     * @return the player who lost the resource
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer target = event.getVictim();
     * }</pre>
     */
    public IPlayer getVictim() {
        return victim;
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

