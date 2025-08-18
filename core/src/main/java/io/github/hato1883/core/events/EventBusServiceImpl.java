package io.github.hato1883.core.events;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.unknown.IAsyncExecutionService;

public class EventBusServiceImpl implements IEventBusService {
    private final EventBusImpl eventBus;
    private volatile boolean shutdown = false;

    public EventBusServiceImpl(IAsyncExecutionService asyncExecutor) {
        this.eventBus = new EventBusImpl(asyncExecutor);
    }

    public EventBusServiceImpl(IAsyncExecutionService asyncExecutor, boolean shouldShutdownExecutor) {
        this.eventBus = new EventBusImpl(asyncExecutor, shouldShutdownExecutor);
    }

    @Override
    public <T extends IEvent> void registerListener(String modId, Class<T> eventType,
                                                    EventPriority priority, IEventListener<T> listener) {
        checkNotShutdown();
        eventBus.registerListener(modId, eventType, priority, listener);
    }

    @Override
    public <T extends IEvent> void unregisterListener(String modId, Class<T> eventType,
                                                      IEventListener<T> listener) {
        if (!shutdown) {
            eventBus.unregisterListener(modId, eventType, listener);
        }
    }

    @Override
    public void unregisterMod(String modId) {
        if (!shutdown) {
            eventBus.unregisterMod(modId);
        }
    }

    @Override
    public <T extends IEvent> void dispatch(T event) {
        checkNotShutdown();
        eventBus.dispatch(event);
    }

    @Override
    public <T extends IEvent> void dispatchAsync(T event) {
        checkNotShutdown();
        eventBus.dispatchAsync(event);
    }

    @Override
    public void shutdown() {
        if (!shutdown) {
            shutdown = true;
            eventBus.shutdown();
        }
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    private void checkNotShutdown() {
        if (shutdown) {
            throw new IllegalStateException("EventBusService has been shutdown");
        }
    }
}
