package io.github.hato1883.api.events;

public interface IEventBus {

    <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener);

    <T extends IEvent> void unregisterListener(String modId, Class<T> eventType, IEventListener<T> listener);

    // Unregister all listeners for a mod
    void unregisterMod(String modId);

    // Dispatch event to all registered listeners for that event class
    <T extends IEvent> void dispatch(T event);

    // Dispatch event to all registered listeners for that event class
    <T extends IEvent> void dispatchAsync(T event);
}
