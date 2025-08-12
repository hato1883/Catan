package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.game.IGameState;

/**
 * Base event class representing lifecycle events related to the game world.
 * This includes initialization, loading, and creation phases.
 *
 * <h3>Used By:</h3>
 * <ul>
 *   <li>WORLD_INIT</li>
 *   <li>WORLD_LOAD</li>
 *   <li>WORLD_CREATED</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(WorldLifecycleEvent.class, event -> {
 *     IGameState gameState = event.getGameState();
 *     System.out.println("A world lifecycle event triggered");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link WorldInitEvent}</li>
 *   <li>{@link WorldLoadEvent}</li>
 *   <li>{@link WorldCreatedEvent}</li>
 * </ul>
 */
public abstract class WorldLifecycleEvent extends GameEvent {
    /**
     * Constructs a new WorldLifecycleEvent.
     *
     * @param gameState The current game state.
     */
    protected WorldLifecycleEvent(IGameState gameState) {
        super(gameState);
    }
}
