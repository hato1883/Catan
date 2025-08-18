package io.github.hato1883.api.events.modding;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.mod.CatanMod;

/**
 * Fired when a mod is disabled.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ModDisableEvent.class, event -> {
 *     System.out.println("Disabled mod: " + event.getMod().getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ModEvent}</li>
 *   <li>{@link IEventBus#registerListener(String, Class, EventPriority, IEventListener)}</li>
 * </ul>
 */
public class ModDisableEvent extends ModEvent implements Cancelable {

    private boolean canceled = false;

    /**
     * Constructs a new ModDisableEvent.
     *
     * @param mod       the mod being disabled
     */
    public ModDisableEvent(CatanMod mod) {
        super(mod);
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

