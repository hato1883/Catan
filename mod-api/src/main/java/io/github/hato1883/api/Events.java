package io.github.hato1883.api;

import io.github.hato1883.api.events.*;
import io.github.hato1883.api.services.IServiceLocator;
import org.jetbrains.annotations.NotNull;

// TODO: Implement remaining methods:
//       registerListeners
//       unregisterListeners
public final class Events {

    private static IServiceLocator serviceProvider;

    // Prevent instantiation
    private Events() {
        throw new UnsupportedOperationException("Events is a utility class");
    }

    /**
     * Dispatches a custom event to all registered listeners.
     * Use this when your mod needs to notify others of something happening.
     */
    public static <T extends IEvent> void dispatch(T event) {
        getEventBus().dispatch(event);
    }
    public static <T extends IEvent> void post(T event) {
        dispatch(event);
    }

    /**
     * Dispatches an event asynchronously.
     * Useful for events that don't need immediate processing.
     */
    public static <T extends IEvent> void dispatchAsync(T event) {
        getEventBus().dispatchAsync(event);
    }
    public static <T extends IEvent> void postAsync(T event) {
        dispatchAsync(event);
    }
    /**
     * Registers an object as an event listener.
     * The object should have methods annotated with @EventListener.
     */
    public static void registerListener(Object listener) {
        getEventListenerRegistrar().registerListeners(listener);
    }
    public static <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener) {
        getEventListenerRegistrar().registerListeners(listener);
    }

    /**
     * Unregisters an event listener object.
     */
    public static void unregisterListener(Object listener) {
        getEventListenerRegistrar().unregisterListeners(listener);
    }

    /**
     * Gets direct access to the event bus for advanced operations.
     */
    public static IEventBusService bus() {
        return getEventBus();
    }

    private static IEventBusService getEventBus() { return  getProvider().require(IEventBusService.class); }
    private static IEventListenerRegistrar getEventListenerRegistrar() { return  getProvider().require(IEventListenerRegistrar.class); }

    public static void initialize(@NotNull IServiceLocator provider) {
        if (serviceProvider != null) {
            throw new IllegalStateException("Events already initialized");
        }
        serviceProvider = provider;
    }

    private static IServiceLocator getProvider() {
        if (serviceProvider == null) {
            throw new IllegalStateException("Events has not been initialized!");
        }
        return serviceProvider;
    }
}
