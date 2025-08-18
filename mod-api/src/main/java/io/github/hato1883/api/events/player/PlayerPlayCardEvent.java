package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;

/**
 * Fired when a player plays a development or ability card.
 * This event is cancellable to prevent execution of the card's effect.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerPlayCardEvent.class, event -> {
 *     String cardId = event.getCardId();
 *     if ("MONOPOLY".equals(cardId)) {
 *         System.out.println("Monopoly card played by " + event.getPlayer().getName());
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link CancellableGameEvent}</li>
 *   <li>{@link PlayerActionEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class PlayerPlayCardEvent extends PlayerActionEvent {
    private final String cardId;

    /**
     * Constructs a new card play event.
     *
     * @param gameState the current game state
     * @param player the player playing the card
     * @param cardId the ID or type of the card
     */
    public PlayerPlayCardEvent(IGameState gameState, IPlayer player, String cardId) {
        super(gameState, player);
        this.cardId = cardId;
    }

    /**
     * The player who played the card.
     *
     * @return the player instance
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * event.getPlayer().getName();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link IPlayer}</li>
     * </ul>
     */
    @Override
    public IPlayer getPlayer() {
        return super.getPlayer();
    }

    /**
     * The identifier of the card played.
     *
     * @return the card ID or type
     *
     * <h3>Defaults:</h3>
     * This is typically a card type like "MONOPOLY", "ROAD_BUILDING", etc.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * String id = event.getCardId();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link PlayerPlayCardEvent}</li>
     * </ul>
     */
    public String getCardId() {
        return cardId;
    }
}

