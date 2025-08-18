package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.network.INetworkMessage;

/**
 * Fired when a network message is received from a connected client or server.
 * <p>
 * This event is triggered after the networking layer successfully parses a message
 * and before it is processed by the game logic.
 * Modders can use this event to inspect, log, or respond to incoming messages.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(NetworkMessageReceivedEvent.class, event -> {
 *     INetworkMessage msg = event.getMessage();
 *     System.out.println("Received: " + msg);
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link NetworkConnectedEvent}</li>
 *   <li>{@link NetworkDisconnectedEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class NetworkMessageReceivedEvent extends NetworkEvent implements Cancelable {

    private final INetworkMessage message;
    private boolean canceled = false;

    /**
     * Creates a new NetworkMessageReceivedEvent.
     *
     * @param message   the network message that was received
     */
    public NetworkMessageReceivedEvent(INetworkMessage message) {
        this.message = message;
    }

    /**
     * Gets the received network message.
     *
     * @return the message
     *
     * <h3>Defaults:</h3>
     * The returned message is exactly what the network layer parsed; no modifications are made.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * INetworkMessage msg = event.getMessage();
     * System.out.println("Received opcode: " + msg.getOpcode());
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link INetworkMessage}</li>
     * </ul>
     */
    public INetworkMessage getMessage() {
        return message;
    }

    /**
     * Checks if this event has been canceled.
     *
     * @return {@code true} if the event has been canceled, {@code false} otherwise.
     */
    @Override
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Cancels this event.
     * <p>
     * Once an event is canceled, it should not be uncanceled.
     * Multiple calls to this method have no additional effect.
     * </p>
     */
    @Override
    public void cancel() {
        canceled = true;
    }
}

