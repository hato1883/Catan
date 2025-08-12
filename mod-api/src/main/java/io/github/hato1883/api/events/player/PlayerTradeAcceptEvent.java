package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.game.event.EventBus;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.IResourceType;

import java.util.Map;

/**
 * Event fired when a player accepts a trade offer.
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PlayerTradeEvent()}</li>
 *   <li>{@link PlayerTradeOfferEvent()}}</li>
 *   <li>{@link PlayerTradeCancelEvent()}}</li>
 *   <li>{@link PlayerUsePortEvent ()}}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class PlayerTradeAcceptEvent extends PlayerTradeEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Constructs a new PlayerTradeAcceptEvent.
     *
     * @param gameState The current game state
     * @param recipient The player receiving the trade
     * @param sender The player who initiated the trade event
     * @param offeredResources Resources the sender is sending to the recipient
     * @param requestedResources Resources the sender is asking for from recipient
     */
    public PlayerTradeAcceptEvent(IGameState gameState, IPlayer recipient, IPlayer sender, Map<IResourceType, Integer> offeredResources, Map<IResourceType, Integer> requestedResources) {
        super(gameState, recipient, sender, offeredResources, requestedResources);
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
