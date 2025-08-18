package io.github.hato1883.api.events.registry;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.Cancelable;
import io.github.hato1883.api.registries.IRegistry;

public class RegistryReplaceEvent<T> extends RegistryEvent<T> implements Cancelable {

    private boolean isCanceled = false;
    private final T oldEntry;

    public RegistryReplaceEvent(IRegistry<T> registry, Identifier id, T oldEntry, T newEntry) {
        super(registry, id, newEntry);
        this.oldEntry = oldEntry;
    }

    /**
     * Retries event that got replaced
     * @return replaced event
     */
    public T getOldEntry() { return oldEntry; }

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
