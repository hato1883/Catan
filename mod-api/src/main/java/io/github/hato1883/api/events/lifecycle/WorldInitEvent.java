package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when the game world is initializing.
 *
 * <h3>Used By:</h3>
 * <ul>
 *   <li>WORLD_INIT</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(WorldInitEvent.class, event -> {
 *     IGameState gameState = event.getGameState();
 *     System.out.println("World is initializing");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link WorldLifecycleEvent}</li>
 *   <li>{@link WorldLoadEvent}</li>
 *   <li>{@link WorldCreatedEvent}</li>
 *   <li>{@link EventBus#registerListener(String, Class, EventPriority, GameEventListener)}</li>
 * </ul>
 */
public class WorldInitEvent extends WorldLifecycleEvent {
    /**
     * Constructs a new WorldInitEvent.
     *
     * @param gameState The current game state.
     */
    public WorldInitEvent(IGameState gameState) {
        super(gameState);
    }
}
