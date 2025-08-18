package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;

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
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
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

