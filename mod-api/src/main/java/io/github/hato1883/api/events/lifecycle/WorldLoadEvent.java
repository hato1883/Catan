package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when the game world has finished loading.
 *
 * <h3>Used By:</h3>
 * <ul>
 *   <li>WORLD_LOAD</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(WorldLoadEvent.class, event -> {
 *     IGameState gameState = event.getGameState();
 *     System.out.println("World has loaded");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link WorldLifecycleEvent}</li>
 *   <li>{@link WorldInitEvent}</li>
 *   <li>{@link WorldCreatedEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class WorldLoadEvent extends WorldLifecycleEvent {
    /**
     * Constructs a new WorldLoadEvent.
     *
     * @param gameState The current game state.
     */
    public WorldLoadEvent(IGameState gameState) {
        super(gameState);
    }
}
