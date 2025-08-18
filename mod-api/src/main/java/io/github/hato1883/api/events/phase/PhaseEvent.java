package io.github.hato1883.api.events.phase;

import io.github.hato1883.api.events.GameplayEvent;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.game.IGameState;

/**
 * Base event class for game phase-related events.
 * Fired when a game phase is entered, updated, or exited.
 * Contains the current game state and the phase involved.
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(PhaseEnterEvent.class, event -> {
 *     IGamePhase phase = event.getPhase();
 *     IGameState state = event.getGameState();
 *     System.out.println("Entered phase: " + phase.getName());
 * });
 * }</pre>
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link PhaseUpdateEvent}</li>
 *   <li>{@link PhaseExitEvent}</li>
 * </ul>
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
     * <h3>Defaults:</h3>
     * Returns the phase that triggered the event.
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IGamePhase phase = event.getPhase();
     * System.out.println("Current phase: " + phase.getName());
     * }</pre>
     */
    public IGamePhase getPhase() {
        return phase;
    }
}
