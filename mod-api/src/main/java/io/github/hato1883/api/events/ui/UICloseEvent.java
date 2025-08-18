package io.github.hato1883.api.events.ui;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.game.IGameState;

/**
 * Fired when a UI component is closed by a player or the system.
 * <p>
 * This event can be used to track when mod or game-specific UIs are dismissed.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(UICloseEvent.class, event -> {
 *     System.out.println("UI Closed: " + event.getUiId());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link UIEvent}</li>
 *     <li>{@link UIOpenEvent}</li>
 *     <li>{@link UIInteractEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class UICloseEvent extends UIEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Creates a new UICloseEvent.
     *
     * @param uiId      The unique identifier for the UI being closed.
     */
    public UICloseEvent(String uiId) {
        super(uiId);
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

