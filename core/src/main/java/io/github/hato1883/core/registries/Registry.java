package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.registries.IRegistry;

import java.util.*;

abstract class Registry<T> implements IRegistry<T> {
    private final Map<Identifier, T> entries = new HashMap<>();
    private final IEventBusService eventBus;

    protected Registry(IEventBusService eventBus) {
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus must not be null");
    }

    @Override
    public T register(Identifier id, T element) {
        if (entries.containsKey(id)) {
            throw new IllegalArgumentException("ID already registered: " + id);
        }

        // Fire pre-register event
        RegistryRegisterEvent<T> event = createRegistryRegisterEvent(id, element);
        eventBus.dispatch(event);
        if (event.isCanceled()) return null;

        // Register element to id
        entries.put(id, element);

        // Fire post-register event (optional)
        return element;
    }

    @Override
    public void replace(Identifier id, T element) {
        if (!entries.containsKey(id)) {
            throw new IllegalArgumentException("ID was not registered: \"" + id + "\" can not be replaced");
        }

        // Fire pre-replace event
        RegistryReplaceEvent<T> event = createRegistryReplaceEvent(id, entries.get(id), element);
        eventBus.dispatch(event);
        if (event.isCanceled()) return;

        // Register element to id
        entries.put(id, element);

        // fire post-replace event (optional)
    }

    @Override
    public Optional<T> get(Identifier id) {
        return Optional.ofNullable(entries.get(id));
    }

    @Override
    public T require(Identifier id) {
        if (entries.containsKey(id))
            return entries.get(id);
        throw new NullPointerException("No such element exists: " + id + ", Full list: " + getAll());
    }

    @Override
    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(entries.values());
    }

    @Override
    public boolean unregister(Identifier id) {
        if (entries.containsKey(id)) {
            // Fire pre-unregister event
            RegistryUnregisterEvent<T> event = createRegistryUnregisterEvent(id, entries.get(id));
            eventBus.dispatch(event);
            if (event.isCanceled()) return false;

            entries.remove(id);

            // Fire post-unregister event (optional)

            return true;
        }
        return false;
    }

    @Override
    public int unregisterAll(String modid) {
        int totalUnregistered = 0;
        for (Identifier key : entries.keySet()) {
            if (key.toString().startsWith(modid)) {
                if (unregister(key))
                    totalUnregistered++;
            }
        }
        return totalUnregistered;
    }

    @Override
    public boolean isRegistered(Identifier id) {
        return entries.containsKey(id);
    }

    // Abstract hook — must be implemented by each specific registry
    protected abstract RegistryRegisterEvent<T> createRegistryRegisterEvent(Identifier id, T element);
    // Abstract hook — must be implemented by each specific registry
    protected abstract RegistryReplaceEvent<T> createRegistryReplaceEvent(Identifier id, T oldElement, T newElement);
    // Abstract hook — must be implemented by each specific registry
    protected abstract RegistryUnregisterEvent<T> createRegistryUnregisterEvent(Identifier id, T element);
}
