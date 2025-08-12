package io.github.hato1883.game.event;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.events.GameEventListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    // Map of event type to list of listeners (sorted by priority)
    private final Map<Class<? extends GameEvent>, List<RegisteredListener<?>>> listeners = new ConcurrentHashMap<>();

    // Tracks listeners per modID for easy unregistration
    private final Map<String, List<RegisteredListener<?>>> listenersByMod = new ConcurrentHashMap<>();

    public <T extends GameEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, GameEventListener<T> listener) {
        RegisteredListener<T> regListener = new RegisteredListener<>(modId, priority, listener, eventType);

        listeners.computeIfAbsent(eventType, k -> new ArrayList<>());
        List<RegisteredListener<?>> eventListeners = listeners.get(eventType);

        eventListeners.add(regListener);
        eventListeners.sort(Comparator.comparing(
            (RegisteredListener<?> rl) -> rl.priority()
        ).reversed());

        listenersByMod.computeIfAbsent(modId, k -> new ArrayList<>()).add(regListener);
    }

    public <T extends GameEvent> void unregisterListener(String modId, Class<T> eventType, GameEventListener<T> listener) {
        List<RegisteredListener<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.removeIf(rl -> rl.modId().equals(modId) && rl.listener().equals(listener));
        }

        List<RegisteredListener<?>> modListeners = listenersByMod.get(modId);
        if (modListeners != null) {
            modListeners.removeIf(rl -> rl.eventType().equals(eventType) && rl.listener().equals(listener));
            if (modListeners.isEmpty()) {
                listenersByMod.remove(modId);
            }
        }
    }

    // Unregister all listeners for a mod
    public void unregisterMod(String modId) {
        List<RegisteredListener<?>> modListeners = listenersByMod.remove(modId);
        if (modListeners != null) {
            for (RegisteredListener<?> rl : modListeners) {
                List<RegisteredListener<?>> eventListeners = listeners.get(rl.eventType());
                if (eventListeners != null) {
                    eventListeners.remove(rl);
                }
            }
        }
    }

    // Dispatch event to all registered listeners for that event class
    public <T extends GameEvent> void post(T event) {
        List<RegisteredListener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) return;

        for (RegisteredListener<?> rl : new ArrayList<>(eventListeners)) {
            @SuppressWarnings("unchecked")
            GameEventListener<T> listener = (GameEventListener<T>) rl.listener();
            listener.onEvent(event);
        }
    }

    // Internal wrapper class for registered listeners
        private record RegisteredListener<T extends GameEvent>(String modId, EventPriority priority,
                                                               GameEventListener<T> listener, Class<T> eventType) {
    }
}
