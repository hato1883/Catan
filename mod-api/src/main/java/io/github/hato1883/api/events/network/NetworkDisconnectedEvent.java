package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;

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
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class NetworkDisconnectedEvent extends NetworkEvent {

    /**
     * Creates a new NetworkDisconnectedEvent.
     */
    public NetworkDisconnectedEvent() {
        super();
    }
}

