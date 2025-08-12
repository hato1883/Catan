package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IStructure;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a structure is about to be upgraded.
 * <p>
 * This event is cancellable — cancelling it will prevent the structure from
 * being upgraded.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(StructureUpgradeEvent.class, event -> {
 *     if (event.getStructure().getOwner().getName().equals("Bob")) {
 *         event.setCancelled(true);
 *         System.out.println("Bob's upgrade was cancelled!");
 *     }
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link StructureEvent}</li>
 *     <li>{@link EventBus#registerListener(Class, GameEventListener)}</li>
 * </ul>
 */
public class StructureUpgradeEvent extends StructureEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Constructs a new StructureUpgradeEvent.
     *
     * @param gameState the current game state
     * @param structure the structure to be upgraded
     */
    public StructureUpgradeEvent(IGameState gameState, IStructure structure) {
        super(gameState, structure);
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

