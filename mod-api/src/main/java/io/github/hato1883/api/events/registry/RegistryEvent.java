package io.github.hato1883.api.events.registry;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.registries.IRegistry;

public abstract class RegistryEvent<T> extends GameEvent {
    private final IRegistry<T> registry;
    private final String id;
    private final T entry;

    protected RegistryEvent(IRegistry<T> registry, String id, T entry) {
        this.registry = registry;
        this.id = id;
        this.entry = entry;
    }

    public IRegistry<T> getRegistry() { return registry; }
    public String getId() { return id; }
    public T getEntry() { return entry; }
}

