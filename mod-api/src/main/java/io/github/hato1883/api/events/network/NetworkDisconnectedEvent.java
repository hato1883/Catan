package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a network connection is closed or lost.
 * <p>
 * This event occurs after the networking layer detects that the connection
 * has been terminated, either intentionally (logout) or unexpectedly.
 * Modders can use this to save data, clean up resources, or attempt reconnection.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(NetworkDisconnectedEvent.class, event -> {
 *     System.out.println("Disconnected from server!");
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link NetworkConnectedEvent}</li>
 *   <li>{@link NetworkMessageReceivedEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class NetworkDisconnectedEvent extends NetworkEvent {

    /**
     * Creates a new NetworkDisconnectedEvent.
     *
     * @param gameState the current game state
     */
    public NetworkDisconnectedEvent(IGameState gameState) {
        super(gameState);
    }
}

