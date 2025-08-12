package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Represents a generic event related to the game board.
 * <p>
 * This is the base class for more specific board events, such as
 * Port connection or Tile revealment.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(StructureUpgradeEvent.class, event -> {
 *     IStructure structure = event.getStructure();
 *     System.out.println("Structure affected: " + structure);
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link GameEvent}</li>
 *     <li>{@link StructureEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public abstract class BoardEvent extends GameEvent {

    /**
     * Constructs a new BoardEvent.
     *
     * @param gameState the current game state
     */
    public BoardEvent(IGameState gameState) {
        super(gameState);
    }
}

