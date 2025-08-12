package io.github.hato1883.api.events.network;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.IPlayer;
import io.github.hato1883.game.event.EventBus;

/**
 * Event fired when a chat message is sent by a player.
 * Allows mods to read or modify chat messages or cancel their sending.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ChatMessageEvent.class, event -> {
 *     System.out.println(event.getSenderName() + ": " + event.getMessage());
 *     if(event.getMessage().contains("spoiler")) {
 *         event.setCancelled(true); // block messages containing spoilers
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link CancellableGameEvent}</li>
 *   <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class ChatMessageEvent extends GameEvent implements Cancelable {
    private final IPlayer sender;
    private String message;
    private boolean canceled = false;

    /**
     * Constructs a new ChatMessageEvent.
     *
     * @param gameState The current game state.
     * @param sender The name of the player sending the message.
     * @param message The chat message content.
     */
    public ChatMessageEvent(IGameState gameState, IPlayer sender, String message) {
        super(gameState);
        this.sender = sender;
        this.message = message;
    }

    /**
     * Gets the player who sent the message.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IPlayer sender = event.getSender();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getMessage()}</li>
     * </ul>
     *
     * @return The player name who sent the message
     */
    public IPlayer getSender() {
        return sender;
    }

    /**
     * Gets the chat message content.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * String msg = event.getMessage();
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #setMessage(String)}</li>
     * </ul>
     *
     * @return The message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the chat message content.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * event.setMessage("[MODDED] " + event.getMessage());
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link #getMessage()}</li>
     * </ul>
     *
     * @param message The new message content
     */
    public void setMessage(String message) {
        this.message = message;
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
