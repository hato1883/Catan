package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a network connection is established between the client and server.
 * <p>
 * This event occurs after the handshake completes and the connection is ready for communication.
 * Modders can use this event to send initial synchronization data or request information from the server.
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
 *   <li>{@link NetworkDisconnectedEvent}</li>
 *   <li>{@link NetworkMessageReceivedEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class NetworkConnectedEvent extends NetworkEvent {

    /**
     * Creates a new NetworkConnectedEvent.
     *
     * @param gameState the current game state
     */
    public NetworkConnectedEvent(IGameState gameState) {
        super(gameState);
    }
}

