package io.github.hato1883.api.events;

import io.github.hato1883.api.world.IGameState;

/**
 * Root event for all Gameplay related events such as:
 *  * <li>Player trading</li>
 *  * <li>Player Rolling dice</li>
 *  * <li>Robber Moved</li>
 *  * <li>Player Build</li>
 *  */
public abstract class GameplayEvent implements IEvent {
    /* add default fields if needed */

    private final IGameState state;

    protected GameplayEvent(IGameState state) {
        this.state = state;
    }


    /**
     * Gets the current game state at the time this event was fired.
     *
     * <h3>Defaults:</h3>
     * Returns the IGameState passed at event creation.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * IGameState state = event.getState();
     * // inspect current player, phase, etc.
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link IGameState}</li>
     * </ul>
     *
     * @return the current game state
     */
    public IGameState getState() {
        return state;
    }
}
