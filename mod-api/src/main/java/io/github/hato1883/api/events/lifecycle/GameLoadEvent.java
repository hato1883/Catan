package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;

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
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
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

