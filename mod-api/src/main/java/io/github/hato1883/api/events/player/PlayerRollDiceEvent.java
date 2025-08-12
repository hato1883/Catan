package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a player rolls dice during their turn.
 * This event can be cancelled to prevent the roll from executing.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PlayerRollDiceEvent.class, event -> {
 *     IPlayer player = event.getPlayer();
 *     System.out.println(player.getName() + " is rolling the dice!");
 *
 *     if (player.isBot()) {
 *         event.setCancelled(true); // Prevent bot from rolling dice
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link CancellableGameEvent}</li>
 *   <li>{@link GameEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, io.github.hato1883.api.events.GameEventListener)}</li>
 * </ul>
 */
public class PlayerRollDiceEvent extends PlayerActionEvent {

    /**
     * Constructs a new dice roll event for the given player.
     *
     * @param gameState the current game state
     * @param player the player rolling the dice
     */
    public PlayerRollDiceEvent(IGameState gameState, IPlayer player) {
        super(gameState, player);
    }

    /**
     * The player performing the dice roll.
     *
     * @return the player
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * event.getPlayer().getId();
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
}

