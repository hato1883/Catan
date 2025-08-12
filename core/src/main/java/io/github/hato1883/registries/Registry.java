package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.registries.IRegistry;
import io.github.hato1883.game.event.EventDispatcher;

import java.util.*;

abstract class Registry<T> implements IRegistry<T> {
    private final Map<String, T> entries = new HashMap<>();

    @Override
    public void register(String id, T element) {
        if (entries.containsKey(id)) {
            throw new IllegalArgumentException("ID already registered: " + id);
        }

        // Fire pre-register event
        RegistryRegisterEvent<T> event = createRegistryRegisterEvent(id, element);
        EventDispatcher.post(event);
        if (event.isCanceled()) return;

        // Register element to id
        entries.put(id, element);

        // Fire post-register event (optional)

    }

    @Override
    public void replace(String id, T element) {
        if (!entries.containsKey(id)) {
            throw new IllegalArgumentException("ID was not registered: \"" + id + "\" can not be replaced");
        }

        // Fire pre-replace event
        RegistryReplaceEvent<T> event = createRegistryReplaceEvent(id, entries.get(id), element);
        EventDispatcher.post(event);
        if (event.isCanceled()) return;

        // Register element to id
        entries.put(id, element);

        // fire post-replace event (optional)
    }

    @Override
    public Optional<T> get(String id) {
        return Optional.ofNullable(entries.get(id));
    }

    @Override
    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(entries.values());
    }

    @Override
    public boolean unregister(String id) {
        if (entries.containsKey(id)) {
            // Fire pre-unregister event
            RegistryUnregisterEvent<T> event = createRegistryUnregisterEvent(id, entries.get(id));
            EventDispatcher.post(event);
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
        for (String key : entries.keySet()) {
            if (key.startsWith(modid)) {
                if (unregister(key))
                    totalUnregistered++;
            }
        }
        return totalUnregistered;
    }

    // Abstract hook — must be implemented by each specific registry
    protected abstract RegistryRegisterEvent<T> createRegistryRegisterEvent(String id, T element);
    // Abstract hook — must be implemented by each specific registry
    protected abstract RegistryReplaceEvent<T> createRegistryReplaceEvent(String id, T oldElement, T newElement);
    // Abstract hook — must be implemented by each specific registry
    protected abstract RegistryUnregisterEvent<T> createRegistryUnregisterEvent(String id, T element);
}
