package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when the game state is being loaded.
 * Allows mods and systems to perform actions or veto loading.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(GameLoadEvent.class, event -> {
 *     System.out.println("Game is loading...");
 *     // Perform custom load logic or veto by cancelling (if cancellable)
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class GameLoadEvent extends GameLifecycleEvent {
    /**
     * Constructs a GameLoadEvent.
     *
     * @param gameState The current game state.
     */
    public GameLoadEvent(IGameState gameState) {
        super(gameState);
    }
}

