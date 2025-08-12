package io.github.hato1883.api.events;

/**
 * Represents an event that can be canceled.
 * <p>
 * When an event is canceled, the action associated with it will not be performed.
 * Event listeners can call {@link #cancel()} to mark the event as canceled.
 * Once canceled, the event remains canceled for the rest of its lifecycle.
 * </p>
 * <p>
 * The default implementation stores the cancellation state internally.
 * Classes implementing this interface may override the methods to provide
 * custom behavior or integrate with other cancellation mechanisms.
 * </p>
 * <h3>Example usage:</h3>
 * <pre>{@code
 * public class PlayerJoinEvent extends GameEvent implements Cancelable {
 *     // No need to implement isCanceled() or cancel() if default behavior is fine.
 * }
 *
 * // Listener example:
 * eventBus.registerListener(PlayerJoinEvent.class, event -> {
 *     if (event.getPlayer().isBanned()) {
 *         event.cancel(); // prevents player from joining
 *     }
 * });
 * }</pre>
 */
public interface Cancelable {

    /**
     * Checks if this event has been canceled.
     *
     * @return {@code true} if the event has been canceled, {@code false} otherwise.
     */
    boolean isCanceled();

    /**
     * Cancels this event.
     * <p>
     * Once an event is canceled, it should not be uncanceled.
     * Multiple calls to this method have no additional effect.
     * </p>
     */
    void cancel();

    /**
     * Default implementation of the {@link Cancelable} interface
     * that stores the cancellation state in a private field.
     * <p>
     * Classes implementing {@link Cancelable} can extend this
     * to inherit default cancellation behavior.
     * </p>
     */
    abstract class CancellableGameEvent implements Cancelable {
        private boolean canceled = false;

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void cancel() {
            this.canceled = true;
        }
    }
}
