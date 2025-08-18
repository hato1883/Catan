package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;

/**
 * Event fired when a player achieves an achievement.
 * Provides information about the player and achievement.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerAchievementEvent.class, event -> {
 *     System.out.println(event.getPlayerName() + " unlocked achievement: " + event.getAchievementName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class PlayerAchievementEvent extends PlayerEvent {
    private final String achievementId;

    /**
     * Constructs a new PlayerAchievementEvent.
     *
     * @param gameState The current game state.
     * @param player The name of the player achieving.
     * @param achievementId The name of the achievement unlocked.
     */
    public PlayerAchievementEvent(IGameState gameState, IPlayer player, String achievementId) {
        super(gameState, player);
        this.achievementId = achievementId;
    }

    /**
     * Gets the achievement ID.
     *
     * <h3>Defaults:</h3>
     * Always set to the achievement being granted.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * String achievement = event.getAchievementId();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getPlayer()}</li>
     * </ul>
     *
     * @return The achievement ID.
     */
    public String getAchievementId() {
        return achievementId;
    }
}


