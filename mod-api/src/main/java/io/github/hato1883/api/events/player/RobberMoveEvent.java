package io.github.hato1883.api.events.player;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.api.game.board.IHexTile;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when a player moves the robber.
 * Fired after the player chooses the new robber location but before the move is finalized.
 * This event supports cancellation to prevent the move.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(RobberMoveEvent.class, event -> {
 *     IPlayer player = event.getPlayer();
 *     IGameState state = event.getGameState();
 *     if (!canMoveRobber(player)) {
 *         event.setCancelled(true);
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PlayerActionEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class RobberMoveEvent extends PlayerActionEvent {
    private final IHexTile newLocation;
    private final IHexTile oldLocation;

    /**
     * Creates a new RobberMoveEvent.
     *
     * @param gameState         the current game state
     * @param player            the player moving the robber
     * @param newLocation the road or tile where the robber is moved
     */
    public RobberMoveEvent(IGameState gameState, IPlayer player, IHexTile newLocation, IHexTile oldLocation) {
        super(gameState, player);
        this.newLocation = newLocation;
        this.oldLocation = oldLocation;
    }

    /**
     * Gets the new Robber location.
     *
     * <h3>Defaults:</h3>
     * The new IHexTile the robber was moved to
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IHexTile newLoc = event.getNewLocation();
     * }</pre>
     *
     * <h3>See Also:</h3>
     */
    public IHexTile getNewLocation () {
        return newLocation;
    }

    /**
     * Gets the old Robber location.
     *
     * <h3>Defaults:</h3>
     * The old IHexTile the robber was moved from
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IHexTile oldLoc = event.getOldLocation();
     * }</pre>
     *
     * <h3>See Also:</h3>
     */
    public IHexTile getOldLocation () {
        return oldLocation;
    }
}

