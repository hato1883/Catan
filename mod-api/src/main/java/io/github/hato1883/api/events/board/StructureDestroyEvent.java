package io.github.hato1883.api.events.board;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.game.board.IStructure;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a structure is about to be destroyed.
 * <p>
 * This event is cancellable — cancelling it will prevent the destruction.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(StructureDestroyEvent.class, event -> {
 *     if ("Main Castle".equals(event.getStructure().getName())) {
 *         event.setCancelled(true);
 *         System.out.println("The castle cannot be destroyed!");
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
public class StructureDestroyEvent extends StructureEvent implements Cancelable {
    private boolean canceled = false;

    /**
     * Constructs a new StructureDestroyEvent.
     *
     * @param gameState the current game state
     * @param structure the structure to be destroyed
     */
    public StructureDestroyEvent(IGameState gameState, IStructure structure) {
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

