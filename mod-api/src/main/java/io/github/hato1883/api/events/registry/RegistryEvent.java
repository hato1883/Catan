package io.github.hato1883.api.events.registry;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.registries.IRegistry;

public abstract class RegistryEvent<T> implements IEvent {
    private final IRegistry<T> registry;
    private final Identifier id;
    private final T entry;

    protected RegistryEvent(IRegistry<T> registry, Identifier id, T entry) {
        this.registry = registry;
        this.id = id;
        this.entry = entry;
    }

    public IRegistry<T> getRegistry() { return registry; }
    public Identifier getId() { return id; }
    public T getEntry() { return entry; }
}

