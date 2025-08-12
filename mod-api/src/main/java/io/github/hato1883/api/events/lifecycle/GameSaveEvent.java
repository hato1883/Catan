package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when the game state is being saved.
 * Allows mods and systems to perform actions or veto saving.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(GameSaveEvent.class, event -> {
 *     System.out.println("Game is saving...");
 *     // Perform custom save logic or veto by cancelling (if cancellable)
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class GameSaveEvent extends GameLifecycleEvent {
    /**
     * Constructs a GameSaveEvent.
     *
     * @param gameState The current game state.
     */
    public GameSaveEvent(IGameState gameState) {
        super(gameState);
    }
}

