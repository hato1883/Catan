package io.github.hato1883.api.events;

public interface IEventListener<T> {
    void onEvent(T event);
}

