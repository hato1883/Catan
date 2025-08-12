package io.github.hato1883.api.events.modding;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.GameEventListener;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.modding.CatanMod;
import io.github.hato1883.game.event.EventBus;

/**
 * Fired when a mod is unloaded from the game environment.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ModUnloadEvent.class, event -> {
 *     System.out.println("Unloaded mod: " + event.getMod().getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ModEvent}</li>
 *   <li>{@link EventBus#registerListener(String, Class, EventPriority, GameEventListener)}</li>
 * </ul>
 */
public class ModUnloadEvent extends ModEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Constructs a new ModUnloadEvent.
     *
     * @param gameState the current game state
     * @param mod       the mod being unloaded
     */
    public ModUnloadEvent(IGameState gameState, CatanMod mod) {
        super(gameState, mod);
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

