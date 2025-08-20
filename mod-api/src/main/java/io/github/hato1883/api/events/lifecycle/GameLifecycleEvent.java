package io.github.hato1883.api.events.lifecycle;

import io.github.hato1883.api.world.IGameState;

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
 *   <li>{@link GameStartEvent}</li>
 *   <li>{@link GameSaveEvent}</li>
 *   <li>{@link GameLoadEvent}</li>
 *   <li>{@link GameEndEvent}</li>
 * </ul>
 */
public abstract class GameLifecycleEvent extends LifecycleEvent {
    /**
     * Constructs a new WorldLifecycleEvent.
     *
     * @param gameState The current game state.
     */
    protected GameLifecycleEvent(IGameState gameState) {
        super(gameState);
    }
}
