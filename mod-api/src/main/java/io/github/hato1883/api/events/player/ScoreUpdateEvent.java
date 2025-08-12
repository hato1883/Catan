package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;
import io.github.hato1883.api.events.lifecycle.VictoryConditionCheckEvent;

/**
 * Fired when a player's score changes.
 * <p>
 * This event is not cancellable and is triggered after the score change
 * has been applied to the game state.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ScoreUpdateEvent.class, event -> {
 *     System.out.println(event.getPlayer().getName() + " now has " + event.getScore() + " points.");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ScoreEvent}</li>
 *   <li>{@link VictoryConditionCheckEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class ScoreUpdateEvent extends ScoreEvent {

    /**
     * Creates a new score update event.
     *
     * @param gameState The current game state.
     * @param player    The player whose score changed.
     * @param score     The player's new score.
     */
    public ScoreUpdateEvent(IGameState gameState, IPlayer player, int score) {
        super(gameState, player, score);
    }
}

