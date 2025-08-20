package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.world.IGameState;

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
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
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
