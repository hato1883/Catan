package io.github.hato1883.core.effects;

import io.github.hato1883.api.effects.*;
import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.events.IEventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EffectManagerTest {
    private EffectManager effectManager;
    private IEventBus eventBus;
    private EffectContext context;
    private IEffectInstance effectInstance;

    @BeforeEach
    void setUp() {
        // Use a simple stub for IEventBus instead of Mockito
        eventBus = new IEventBus() {
            @Override
            public <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener) {}
            @Override
            public <T extends IEvent> void unregisterListener(String modId, Class<T> eventType, IEventListener<T> listener) {}
            @Override
            public void unregisterMod(String modId) {}
            @Override
            public <T extends IEvent> void dispatch(T event) {}
            @Override
            public <T extends IEvent> void dispatchAsync(T event) {}
            @Override
            public <T extends IEvent> void dispatchOnMainThread(T event) {}
        };
        effectManager = new EffectManager(eventBus);
        context = new EffectContext() {};
        effectInstance = new IEffectInstance() {
            @Override
            public IEffectType getType() {
                return new IEffectType() {
                    @Override
                    public String getId() {
                        return "test";
                    }

                    @Override
                    public Optional<IEffectType> getParent() {
                        return Optional.empty();
                    }

                    @Override
                    public boolean isEnabled(EffectContext context) {
                        return false;
                    }

                    @Override
                    public String toString() {
                        return getId();
                    }
                };
            }
            @Override
            public Map<String, Object> getMetadata() {
                return Collections.<String, Object>emptyMap();
            }
        };
    }

    @Test
    void testApplyAndGetActiveEffects() {
        effectManager.applyEffect(context, effectInstance);
        List<IEffectInstance> effects = effectManager.getActiveEffects(context);
        assertEquals(1, effects.size());
        assertTrue(effects.contains(effectInstance));
    }

    @Test
    void testClearEffects() {
        effectManager.applyEffect(context, effectInstance);
        effectManager.clearEffects(context);
        List<IEffectInstance> effects = effectManager.getActiveEffects(context);
        assertTrue(effects.isEmpty());
    }
}
