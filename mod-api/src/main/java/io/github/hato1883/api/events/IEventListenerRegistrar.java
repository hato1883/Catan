package io.github.hato1883.api.events;

public interface IEventListenerRegistrar {

    /**
     * Scans the given base package for classes with methods annotated with @EventListener,
     * instantiates them with a no-arg constructor,
     * and registers their listener methods on the EventDispatcher using the provided modId.
     *
     * @param modId       the mod ID to register listeners under
     * @param basePackage the package prefix to scan for listener classes, e.g. "io.github.hato1883.game.logic"
     */
    void registerListenersInPackage(String modId, String basePackage);

    /**
     * Registers all methods annotated with @EventListener in the given listener instance.
     *
     * @param modId    the mod ID to register listeners under
     * @param listener the listener instance with @EventListener methods
     */
    void register(String modId, Object listener);

    /**
     * Registers all methods annotated with @EventListener in the given listener instance.
     *
     * @param listener the listener instance with @EventListener methods
     */
    void registerListeners(Object listener);

    <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener);

    /**
     * Removes all Listeners from event bus from the given instance.
     *
     * @param listener the listener instance with @EventListener methods
     */
    void unregisterListeners(Object listener);
}
