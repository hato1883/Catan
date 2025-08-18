package io.github.hato1883.api.events;

public interface IEventBusService extends IEventBus {
    void shutdown();
    boolean isShutdown();
}
