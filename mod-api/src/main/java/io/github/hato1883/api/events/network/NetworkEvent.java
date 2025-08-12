package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a network any network event occurs.
 * <p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(NetworkConnectedEvent.class, event -> {
 *     System.out.println("Connected to server!");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link NetworkConnectedEvent}</li>
 *   <li>{@link NetworkDisconnectedEvent}</li>
 *   <li>{@link NetworkMessageReceivedEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class NetworkEvent extends GameEvent {

    /**
     * Creates a new NetworkConnectedEvent.
     *
     * @param gameState the current game state
     */
    public NetworkEvent(IGameState gameState) {
        super(gameState);
    }
}

