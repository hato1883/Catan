package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;

/**
 * Event fired when the game world has been created.
 *
 * <h3>Used By:</h3>
 * <ul>
 *   <li>WORLD_CREATED</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(WorldCreatedEvent.class, event -> {
 *     IGameState gameState = event.getGameState();
 *     System.out.println("World has been created");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link WorldLifecycleEvent}</li>
 *   <li>{@link WorldInitEvent}</li>
 *   <li>{@link WorldLoadEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
 * </ul>
 */
public class WorldCreatedEvent extends WorldLifecycleEvent {
    /**
     * Constructs a new WorldCreatedEvent.
     *
     * @param gameState The current game state.
     */
    public WorldCreatedEvent(IGameState gameState) {
        super(gameState);
    }
}
