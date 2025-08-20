package io.github.hato1883.core.events.bus;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.hato1883.api.Services;
import io.github.hato1883.api.events.*;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.core.bootstrap.services.ServiceBootstrap;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class EventListenerRegistrar implements IEventListenerRegistrar {
    private IEventBusService eventBusService;

    /**
     * Scans the given base package for classes with methods annotated with @EventListener,
     * instantiates them with a no-arg constructor,
     * and registers their listener methods on the IEventBus using the provided modId.
     *
     * @param modId       the mod ID to register listeners under
     * @param basePackage the package prefix to scan for listener classes, e.g. "io.github.hato1883.game.logic"
     */
    public void registerListenersInPackage(String modId, String basePackage) {
        try (ScanResult scanResult = new ClassGraph()
            .enableAllInfo()
            .acceptPackages(basePackage)
            .scan()) {

            scanResult.getClassesWithMethodAnnotation(EventListener.class.getName())
                .forEach(classInfo -> {
                    try {
                        Class<?> clazz = classInfo.loadClass();
                        Object listener = clazz.getDeclaredConstructor().newInstance();
                        registerListenerMethods(modId, listener);
                    } catch (Exception e) {
                        LogManager.getLogger(modId).error("Failed to instantiate or register listener class '{}': {}",
                            classInfo.getName(), e.getMessage(), e);
                    }
                });
        }
    }

    /**
     * Registers all methods annotated with @EventListener in the given listener instance.
     *
     * @param modId    the mod ID to register listeners under
     * @param listener the listener instance with @EventListener methods
     */
    public void register(String modId, Object listener) {
        registerListenerMethods(modId, listener);
    }

    @Override
    public void registerListeners(Object listener) {
        // TODO
    }

    @Override
    public void unregisterListeners(Object listener) {
        // TODO
    }

    /**
     * Common logic for scanning methods on the listener instance,
     * validating, and registering them with the IEventBus.
     *
     * @param modId    the mod ID for logging and registration
     * @param listener the instance containing listener methods
     */
    private void registerListenerMethods(String modId, Object listener) {
        Logger modLogger = LogManager.getLogger(modId);

        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventListener.class)) continue;

            Class<? extends IEvent> eventType;
            try {
                eventType = getValidEventType(method);
            } catch (IllegalArgumentException e) {
                if (modLogger.isErrorEnabled()) {
                    modLogger.error("Invalid @EventListener in method '{}' of class '{}': {}",
                        method.getName(),
                        method.getDeclaringClass().getName(),
                        e.getMessage());
                }
                if (modLogger.isTraceEnabled()) {
                    modLogger.trace("Invalid method signature stack trace:", e);
                }
                continue;
            }

            method.setAccessible(true);
            EventListener annotation = method.getAnnotation(EventListener.class);
            EventPriority priority = annotation.priority();

            registerListener(modId, eventType, priority, event -> {
                try {
                    method.invoke(listener, event);
                } catch (Exception ex) {
                    modLogger.error("Exception invoking listener method '{}' in class '{}': {}",
                        method.getName(),
                        listener.getClass().getName(),
                        ex.getMessage(), ex);
                    if (modLogger.isTraceEnabled()) {
                        modLogger.trace("Invocation exception stack trace:", ex);
                    }
                }
            });
        }
    }

    public <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener) {
        if (eventBusService == null)
            eventBusService = Services.require(IEventBusService.class);
        eventBusService.registerListener(modId, eventType, priority, listener);
    }

    private Class<? extends IEvent> getValidEventType(Method method) {
        Parameter[] params = method.getParameters();
        if (params.length != 1) {
            throw new IllegalArgumentException("Method " + method + " must have exactly one parameter");
        }

        Class<?> paramType = params[0].getType();
        if (!IEvent.class.isAssignableFrom(paramType)) {
            throw new IllegalArgumentException("Parameter type " + paramType + " is not a subclass of IEvent");
        }
        return paramType.asSubclass(IEvent.class);
    }
}
