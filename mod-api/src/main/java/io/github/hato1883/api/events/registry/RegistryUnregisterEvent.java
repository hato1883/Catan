package io.github.hato1883.api.events.registry;

import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.registries.IRegistry;

public class RegistryUnregisterEvent<T> extends RegistryEvent<T> implements Cancelable {

    private boolean isCanceled = false;

    public RegistryUnregisterEvent(IRegistry<T> registry, String id, T entry) {
        super(registry, id, entry);
    }

    /**
     * Checks if this event has been canceled.
     *
     * @return {@code true} if the event has been canceled, {@code false} otherwise.
     */
    @Override
    public boolean isCanceled() {
        return isCanceled;
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
        isCanceled = true;
    }
}
