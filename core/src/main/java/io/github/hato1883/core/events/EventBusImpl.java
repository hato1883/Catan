package io.github.hato1883.core.events;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.unknown.IAsyncExecutionService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class EventBusImpl implements IEventBus {
    // Map of event type to list of listeners (sorted by priority)
    private final Map<Class<? extends IEvent>, List<RegisteredListener<?>>> listeners = new ConcurrentHashMap<>();

    // Tracks listeners per modID for easy un-registration
    private final Map<String, List<RegisteredListener<?>>> listenersByMod = new ConcurrentHashMap<>();

    // Executor service for async event dispatching
    private final IAsyncExecutionService asyncExecutor;
    private final boolean shouldShutdownExecutor;

    // Constructor with custom executor (follows DIP - depend on abstraction)
    public EventBusImpl(IAsyncExecutionService asyncExecutor) {
        this(asyncExecutor, false);
    }

    // Constructor with custom executor and ownership control
    public EventBusImpl(IAsyncExecutionService asyncExecutor, boolean shouldShutdownExecutor) {
        this.asyncExecutor = Objects.requireNonNull(asyncExecutor, "asyncExecutor cannot be null");
        this.shouldShutdownExecutor = shouldShutdownExecutor;
    }

    @Override
    public <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener) {
        validateRegisterParameters(modId, eventType, priority, listener);

        RegisteredListener<T> regListener = new RegisteredListener<>(modId, priority, listener, eventType);
        addListenerToMaps(regListener, eventType);
    }

    @Override
    public <T extends IEvent> void unregisterListener(String modId, Class<T> eventType, IEventListener<T> listener) {
        validateUnregisterParameters(modId, eventType, listener);
        removeListenerFromMaps(modId, eventType, listener);
    }

    @Override
    public void unregisterMod(String modId) {
        if (modId == null || modId.trim().isEmpty()) {
            throw new IllegalArgumentException("modId cannot be null or empty");
        }

        List<RegisteredListener<?>> modListeners = listenersByMod.remove(modId);
        if (modListeners != null) {
            removeModListenersFromEventMap(modListeners);
        }
    }

    @Override
    public <T extends IEvent> void dispatch(T event) {
        validateEvent(event);
        List<RegisteredListener<?>> eventListeners = getEventListeners(event);
        if (eventListeners.isEmpty()) return;

        dispatchToListeners(event, eventListeners);
    }

    @Override
    public <T extends IEvent> void dispatchAsync(T event) {
        validateEvent(event);
        List<RegisteredListener<?>> eventListeners = getEventListeners(event);
        if (eventListeners.isEmpty()) return;

        // Submit async task that captures the current state of listeners
        asyncExecutor.executeAsync(() -> dispatchToListeners(event, eventListeners), "event-dispatch");
    }

    // Shutdown method for proper resource cleanup
    public void shutdown() {
        if (shouldShutdownExecutor && !asyncExecutor.isShutdown()) {
            asyncExecutor.shutdown();
            try {
                asyncExecutor.awaitTermination(5, TimeUnit.SECONDS);
                asyncExecutor.shutdown();
            } catch (InterruptedException e) {
                asyncExecutor.shutdown();
                Thread.currentThread().interrupt();
            }
        }
    }

    // Private helper methods (SRP - single responsibility for each method)

    private <T extends IEvent> void validateRegisterParameters(String modId, Class<T> eventType,
                                                               EventPriority priority, IEventListener<T> listener) {
        if (modId == null || modId.trim().isEmpty()) {
            throw new IllegalArgumentException("modId cannot be null or empty");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("priority cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
    }

    private <T extends IEvent> void validateUnregisterParameters(String modId, Class<T> eventType,
                                                                 IEventListener<T> listener) {
        if (modId == null || modId.trim().isEmpty()) {
            throw new IllegalArgumentException("modId cannot be null or empty");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
    }

    private <T extends IEvent> void validateEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }
    }

    private <T extends IEvent> void addListenerToMaps(RegisteredListener<T> regListener, Class<T> eventType) {
        // Add to event type map with priority sorting
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>());
        List<RegisteredListener<?>> eventListeners = listeners.get(eventType);

        eventListeners.add(regListener);
        sortListenersByPriority(eventListeners);

        // Add to mod tracking map
        listenersByMod.computeIfAbsent(regListener.modId(), k -> new ArrayList<>()).add(regListener);
    }

    private void sortListenersByPriority(List<RegisteredListener<?>> eventListeners) {
        eventListeners.sort(Comparator.comparing(
            (RegisteredListener<?> rl) -> rl.priority()
        ).reversed());
    }

    private <T extends IEvent> void removeListenerFromMaps(String modId, Class<T> eventType,
                                                           IEventListener<T> listener) {
        removeFromEventListeners(modId, eventType, listener);
        removeFromModListeners(modId, eventType, listener);
    }

    private <T extends IEvent> void removeFromEventListeners(String modId, Class<T> eventType,
                                                             IEventListener<T> listener) {
        List<RegisteredListener<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.removeIf(rl -> rl.modId().equals(modId) && rl.listener().equals(listener));
        }
    }

    private <T extends IEvent> void removeFromModListeners(String modId, Class<T> eventType,
                                                           IEventListener<T> listener) {
        List<RegisteredListener<?>> modListeners = listenersByMod.get(modId);
        if (modListeners != null) {
            modListeners.removeIf(rl -> rl.eventType().equals(eventType) && rl.listener().equals(listener));
            if (modListeners.isEmpty()) {
                listenersByMod.remove(modId);
            }
        }
    }

    private void removeModListenersFromEventMap(List<RegisteredListener<?>> modListeners) {
        for (RegisteredListener<?> rl : modListeners) {
            List<RegisteredListener<?>> eventListeners = listeners.get(rl.eventType());
            if (eventListeners != null) {
                eventListeners.remove(rl);
                if (eventListeners.isEmpty()) {
                    listeners.remove(rl.eventType());
                }
            }
        }
    }

    private <T extends IEvent> List<RegisteredListener<?>> getEventListeners(T event) {
        List<RegisteredListener<?>> eventListeners = listeners.get(event.getClass());
        return eventListeners != null ? new ArrayList<>(eventListeners) : Collections.emptyList();
    }

    private <T extends IEvent> void dispatchToListeners(T event, List<RegisteredListener<?>> eventListeners) {
        for (RegisteredListener<?> rl : eventListeners) {
            try {
                @SuppressWarnings("unchecked")
                IEventListener<T> listener = (IEventListener<T>) rl.listener();
                listener.onEvent(event);
            } catch (Exception e) {
                // Log error but continue processing other listeners
                // In a real implementation, you might want to use a proper logger
                System.err.println("Error dispatching event to listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Internal wrapper class for registered listeners (immutable record)
    private record RegisteredListener<T extends IEvent>(
        String modId,
        EventPriority priority,
        IEventListener<T> listener,
        Class<T> eventType
    ) {}
}
