package io.github.hato1883.api.events.player;

import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.IResourceType;

import java.util.Map;

/**
 * Base event class for all player trade related events.
 * Represents any trade interaction initiated by a player, including offering, accepting, and canceling trades.
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PlayerTradeOfferEvent()}</li>
 *   <li>{@link PlayerTradeAcceptEvent()}}</li>
 *   <li>{@link PlayerTradeCancelEvent()}}</li>
 *   <li>{@link PlayerUsePortEvent ()}}</li>
 * </ul>
 */
public abstract class PlayerTradeEvent extends PlayerEvent {
    protected final IPlayer recipient;
    protected final Map<IResourceType, Integer> offeredResources;
    protected final Map<IResourceType, Integer> requestedResources;

    /**
     * Constructs a new PlayerTradeEvent.
     *
     * @param gameState The current game state
     * @param recipient The player receiving the trade
     * @param sender The player who initiated the trade event
     * @param offeredResources Resources the sender is sending to the recipient
     * @param requestedResources Resources the sender is asking for from recipient
     */
    public PlayerTradeEvent(IGameState gameState, IPlayer recipient, IPlayer sender, Map<IResourceType, Integer> offeredResources, Map<IResourceType, Integer> requestedResources) {
        super(gameState, sender);
        this.recipient = recipient;
        this.requestedResources = requestedResources;
        this.offeredResources = offeredResources;
    }

    /**
     * Returns the player who is receiving the trade.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer recipient = event.getRecipient();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getSender()}</li>
     *   <li>{@link #getOfferedResources()} ()}</li>
     *   <li>{@link #getRequestedResources()} ()}</li>
     * </ul>
     *
     * @return The player who is receiving the trade
     */
    public IPlayer getRecipient() {
        return recipient;
    }

    /**
     * Returns the player who triggered this trade event.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer sender = event.getSender();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getRecipient()}</li>
     *   <li>{@link #getOfferedResources()}</li>
     *   <li>{@link #getRequestedResources()} ()}</li>
     * </ul>
     *
     * @return The player who initiated the trade event
     */
    public IPlayer getSender() {
        return super.getPlayer();
    }

    /**
     * @deprecated This method is ambiguous in the context of a trade.
     * Use {@link #getSender()} or {@link #getRecipient()} instead.
     * Calling this method will throw an exception.
     * @return Nothing, throws exception.
     * @throws UnsupportedOperationException use {@link #getSender()} or {@link #getRecipient()} instead.
     */
    @Override
    @Deprecated
    public IPlayer getPlayer(){
        throw new UnsupportedOperationException(
                "PlayerTradeEvent does not support getPlayer(). Use getSender() or getReceiver() instead."
        );
    }

    /**
     * Gets the resources offered by the sender.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * Map<IResourceType, Integer> offered = event.getOfferedResources();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getSender()} ()}</li>
     *   <li>{@link #getRecipient()}</li>
     *   <li>{@link #getRequestedResources()} ()}</li>
     * </ul>
     *
     * @return The resources offered by the sender
     */
    public Map<IResourceType, Integer> getOfferedResources() {
        return offeredResources;
    }


    /**
     * Gets the resources requested by the sender.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * Map<IResourceType, Integer> requested = event.getOfferedResources();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getSender()} ()} ()}</li>
     *   <li>{@link #getRecipient()}</li>
     *   <li>{@link #getOfferedResources()}</li>
     * </ul>
     *
     * @return The resources requested by the sender
     */
    public Map<IResourceType, Integer> getRequestedResources() {
        return offeredResources;
    }
}
