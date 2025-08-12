package io.github.hato1883.api.events;

public interface GameEventListener<T> {
    void onEvent(T event);
}

