package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;
import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.TilePosition;

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
 *     ITile tile = event.getTile();
 *     TilePosition pos = tile.getPosition();
 *     System.out.println("Tile revealed at position: (" + pos.x + ", " + pos.y + ", " + pos.z + ")");
 *
 *     // Example: Cancel reveal under special mod rules
 *     if (tile.getType().isVolcano()) {
 *         System.out.println("Volcano tiles are never revealed in this mod!");
 *         event.setCancelled(true);
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link BoardEvent}</li>
 *     <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
 * </ul>
 */
public class TileRevealEvent extends BoardEvent implements Cancelable {

    private final ITile tile;
    private boolean canceled = false;

    /**
     * Creates a new {@code TileRevealEvent}.
     *
     * @param tile the tile being revealed
     * @param state the current game state
     */
    public TileRevealEvent(ITile tile, IGameState state) {
        super(state);
        this.tile = tile;
    }

    /**
     * Gets the tile being revealed.
     *
     * @return the {@link ITile} that is being revealed
     *
     * <h3>Defaults:</h3>
     * This method simply returns the tile associated with this event.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * ITile revealedTile = event.getTile();
     * System.out.println("Revealed terrain: " + revealedTile.getTerrainType());
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link ITile}</li>
     *   <li>{@link #getState()}</li>
     * </ul>
     */
    public ITile getTile() {
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
