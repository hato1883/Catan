package io.github.hato1883.api.events.ui;

import io.github.hato1883.api.events.IEvent;

/**
 * Base class for all user interface-related events.
 * <p>
 * This event type is triggered whenever a UI element in the game lifecycle
 * is opened, closed, or interacted with.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(UIEvent.class, event -> {
 *     System.out.println("UI Event triggered: " + event.getUiId());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link UIOpenEvent}</li>
 *     <li>{@link UICloseEvent}</li>
 *     <li>{@link UIInteractEvent}</li>
 * </ul>
 */
public abstract class UIEvent implements IEvent {

    private final String uiId;

    /**
     * Creates a new UIEvent.
     *
     * @param uiId      The unique identifier for the UI component.
     */
    public UIEvent(String uiId) {
        this.uiId = uiId;
    }

    /**
     * Gets the unique identifier of the UI component related to this event.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * String id = event.getUiId();
     * if ("inventory".equals(id)) {
     *     // handle inventory UI
     * }
     * }</pre>
     *
     * @return the UI component ID
     */
    public String getUiId() {
        return uiId;
    }
}

