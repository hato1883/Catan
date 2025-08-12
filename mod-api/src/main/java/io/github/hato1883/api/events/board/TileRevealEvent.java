package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IHexTile;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a hex tile is revealed to players, usually due to exploration or specific
 * game mechanics that uncover hidden map areas.
 * <p>
 * This event is triggered <strong>after</strong> the tile has been determined for reveal,
 * but before any resulting effects (such as resource production or structure placement) occur.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(TileRevealEvent.class, event -> {
 *     IHexTile tile = event.getTile();
 *     System.out.println("Tile revealed at position: " + tile.getPosition());
 *
 *     // Example: Cancel reveal under special mod rules
 *     if (tile.getTerrainType().isVolcano()) {
 *         System.out.println("Volcano tiles are never revealed in this mod!");
 *         event.setCancelled(true);
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link GameEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, io.github.hato1883.api.events.GameEventListener)}</li>
 * </ul>
 */
public class TileRevealEvent extends BoardEvent implements Cancelable {

    private final IHexTile tile;
    private boolean canceled = false;

    /**
     * Creates a new {@code TileRevealEvent}.
     *
     * @param gameState the current game state
     * @param tile the tile being revealed
     */
    public TileRevealEvent(IGameState gameState, IHexTile tile) {
        super(gameState);
        this.tile = tile;
    }

    /**
     * Gets the tile being revealed.
     *
     * @return the {@link IHexTile} that is being revealed
     *
     * <h3>Defaults:</h3>
     * This method simply returns the tile associated with this event.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IHexTile revealedTile = event.getTile();
     * System.out.println("Revealed terrain: " + revealedTile.getTerrainType());
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link IHexTile}</li>
     *   <li>{@link #getGameState()}</li>
     * </ul>
     */
    public IHexTile getTile() {
        return tile;
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

