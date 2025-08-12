package io.github.hato1883.game.event;

import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.events.GameEventListener;

public class EventDispatcher {
    public static final EventBus GLOBAL_BUS = new EventBus();

    public static <T extends GameEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, GameEventListener<T> listener) {
        GLOBAL_BUS.registerListener(modId, eventType, priority, listener);
    }

    public static void unregisterModListeners(String modId) {
        GLOBAL_BUS.unregisterMod(modId);
    }

    public static <T extends GameEvent> void post(T event) {
        GLOBAL_BUS.post(event);
    }
}
