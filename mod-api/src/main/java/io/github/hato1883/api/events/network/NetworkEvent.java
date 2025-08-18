package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.events.IEvent;

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
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public abstract class NetworkEvent implements IEvent {}

