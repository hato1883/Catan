package io.github.hato1883.api.events.ui;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;

/**
 * Fired when the player interacts with a UI component.
 * <p>
 * This event supports cancellation, allowing mods to prevent certain UI interactions.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(UIInteractEvent.class, event -> {
 *     if ("restricted_button".equals(event.getElementId())) {
 *         event.setCancelled(true);
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link UIEvent}</li>
 *     <li>{@link UIOpenEvent}</li>
 *     <li>{@link UICloseEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li> * </ul>
 * </ul>
 */
public class UIInteractEvent extends UIEvent implements Cancelable {

    private final String elementId;
    private boolean canceled = false;

    /**
     * Creates a new UIInteractEvent.
     *
     * @param uiId      The unique identifier for the UI being interacted with.
     * @param elementId The unique identifier for the specific UI element clicked or used.
     */
    public UIInteractEvent(String uiId, String elementId) {
        super(uiId);
        this.elementId = elementId;
    }

    /**
     * Gets the unique identifier for the specific element within the UI.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * if ("submit_button".equals(event.getElementId())) {
     *     // Submit form logic
     * }
     * }</pre>
     *
     * @return The UI element ID.
     */
    public String getElementId() {
        return elementId;
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

