package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;

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
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class NetworkConnectedEvent extends NetworkEvent {

    /**
     * Creates a new NetworkConnectedEvent.
     */
    public NetworkConnectedEvent() {
        super();
    }
}

