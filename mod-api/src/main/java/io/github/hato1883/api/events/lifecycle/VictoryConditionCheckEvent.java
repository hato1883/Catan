package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.entities.player.IPlayer;
import io.github.hato1883.api.events.player.ScoreEvent;
import io.github.hato1883.api.events.player.ScoreUpdateEvent;

/**
 * Fired when the game checks whether a player meets a victory condition.
 * <p>
 * This event is cancellable — cancelling it will prevent the game from
 * declaring the player as the winner at this time.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(VictoryConditionCheckEvent.class, event -> {
 *     if ("Bob".equals(event.getPlayer().getName())) {
 *         event.setCancelled(true); // Bob can never win... sorry, Bob.
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ScoreEvent}</li>
 *   <li>{@link ScoreUpdateEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
 * </ul>
 */
public class VictoryConditionCheckEvent extends ScoreEvent implements Cancelable {

    private boolean canceled;

    /**
     * Creates a new victory condition check event.
     *
     * @param gameState The current game state.
     * @param player    The player being checked for victory.
     * @param score     The player's score.
     */
    public VictoryConditionCheckEvent(IGameState gameState, IPlayer player, int score) {
        super(gameState, player, score);
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

