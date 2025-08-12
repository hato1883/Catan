package io.github.hato1883.api.events;

import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.game.event.EventBus;

/**
 * Event triggered when a random game event occurs such as a flood or earthquake.
 * This event is fired at the moment the random event is about to take effect.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 *     @EventListener(priority = EventPriority.HIGH)
 *     public void onRandomOccurrenceThing(RandomEventTriggerEvent event) {
 *         // Method name dose not need to match Event name
 *         System.out.println("The following evnt has started: " + event.getEventName());
 *     }
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link EventListener} for automatic registration or use </li>
 *   <li>{@link EventBus#registerListener(String, Class, EventPriority, GameEventListener)} for manuel registration</li>
 * </ul>
 */
public class RandomEventTriggerEvent extends GameEvent implements Cancelable {
    private final String eventName;
    private boolean canceled = false;

    /**
     * Constructs a new RandomEventTriggerEvent.
     *
     * @param gameState The current game state when the event is fired.
     * @param eventName The name of the random event (e.g., "Flood", "Earthquake").
     */
    public RandomEventTriggerEvent(IGameState gameState, String eventName) {
        super(gameState);
        this.eventName = eventName;
    }

    /**
     * Gets the name of the random event triggered.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * String eventName = event.getEventName();
     * System.out.println("Random event: " + eventName);
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link RandomEventTriggerEvent}</li>
     * </ul>
     *
     * @return The name of the random event
     */
    public String getEventName() {
        return eventName;
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

