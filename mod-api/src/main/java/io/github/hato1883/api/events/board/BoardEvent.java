package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.GameplayEvent;
import io.github.hato1883.api.game.IGameState;

/**
 * Represents a generic event related to the game board.
 * <p>
 * This is the base class for more specific board events, such as
 * Port connection or Tile reveal.
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
 *     <li>{@link GameplayEvent}</li>
 *     <li>{@link StructureEvent}</li>
 * </ul>
 */
public abstract class BoardEvent extends GameplayEvent {

    /**
     * Constructs a new BoardEvent.
     *
     * @param gameState the current game state
     */
    public BoardEvent(IGameState gameState) {
        super(gameState);
    }
}

