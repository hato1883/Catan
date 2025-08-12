package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.events.lifecycle.VictoryConditionCheckEvent;

/**
 * Base class for events related to a player's score in the game.
 * <p>
 * This event contains common data for score-related actions, including the
 * {@link IGameState} and the player whose score is being processed.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ScoreUpdateEvent.class, event -> {
 *     IPlayer player = event.getPlayer();
 *     int score = event.getScore();
 *     System.out.println(player.getName() + " now has " + score + " points.");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link VictoryConditionCheckEvent}</li>
 *   <li>{@link ScoreUpdateEvent}</li>
 * </ul>
 */
public abstract class ScoreEvent extends GameEvent {

    private final IPlayer player;
    private final int score;

    /**
     * Creates a new score event.
     *
     * @param gameState The current game state.
     * @param player    The player whose score is being processed.
     * @param score     The current score of the player.
     */
    public ScoreEvent(IGameState gameState, IPlayer player, int score) {
        super(gameState);
        this.player = player;
        this.score = score;
    }

    /**
     * Gets the player whose score is being processed.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer p = event.getPlayer();
     * }</pre>
     *
     * @return The player.
     */
    public IPlayer getPlayer() {
        return player;
    }

    /**
     * Gets the current score of the player.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * int score = event.getScore();
     * }</pre>
     *
     * @return The score value.
     */
    public int getScore() {
        return score;
    }
}

