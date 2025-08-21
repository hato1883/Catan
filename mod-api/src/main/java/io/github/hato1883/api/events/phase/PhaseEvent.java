package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.GameplayEvent;
import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.world.IGameState;

/**
 * Base event class for game phase-related events.
 * Contains the current game state and the phase involved.
 *
 * <p>Example usage:</p>
 * <pre>
 *   eventBus.registerListener(PhaseStartEvent.class, event -> {
 *       // Handle phase start
 *   });
 *   eventBus.registerListener(PhaseEndEvent.class, event -> {
 *       // Handle phase end
 *   });
 * </pre>
 *
 * <p>Note: This is an abstract class and cannot be instantiated directly.</p>
 *
 * @see PhaseStartEvent
 * @see PhaseEndEvent
 */
public abstract class PhaseEvent extends GameplayEvent {
    private final IGamePhase phase;

    /**
     * Constructs a phase event.
     *
     * @param gameState current game state
     * @param phase the phase involved in this event
     */
    protected PhaseEvent(IGameState gameState, IGamePhase phase) {
        super(gameState);
        this.phase = phase;
    }

    /**
     * Gets the game phase involved in this event.
     *
     * @return the current {@link IGamePhase}
     */
    public IGamePhase getPhase() {
        return phase;
    }
}
