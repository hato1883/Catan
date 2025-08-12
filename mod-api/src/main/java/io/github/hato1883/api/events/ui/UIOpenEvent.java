package io.github.hato1883.api.events.ui;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a UI component is opened by a player or the system.
 * <p>
 * This event can be used to track when mod or game-specific UIs are shown to the player.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(UIOpenEvent.class, event -> {
 *     System.out.println("UI Opened: " + event.getUiId());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link UIEvent}</li>
 *     <li>{@link UIInteractEvent}</li>
 *     <li>{@link UICloseEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class UIOpenEvent extends UIEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Creates a new UIOpenEvent.
     *
     * @param gameState The current game state.
     * @param uiId      The unique identifier for the UI being opened.
     */
    public UIOpenEvent(IGameState gameState, String uiId) {
        super(gameState, uiId);
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

