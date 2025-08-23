package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEvent;
import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.events.EventPriority;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.services.IServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DummyRegistry extends Registry<String> {
    public DummyRegistry(IEventBusService eventBus) {
        super(eventBus);
    }
    @Override
    protected RegistryRegisterEvent<String> createRegistryRegisterEvent(Identifier id, String element) {
        return new RegistryRegisterEvent<>(null, id, element) {
            @Override public boolean isCanceled() { return false; }
            @Override public void cancel() {}
        };
    }
    @Override
    protected RegistryReplaceEvent<String> createRegistryReplaceEvent(Identifier id, String oldElement, String newElement) {
        return new RegistryReplaceEvent<>(null, id, oldElement, newElement) {
            @Override public boolean isCanceled() { return false; }
            @Override public void cancel() {}
        };
    }
    @Override
    protected RegistryUnregisterEvent<String> createRegistryUnregisterEvent(Identifier id, String element) {
        return new RegistryUnregisterEvent<>(null, id, element) {
            @Override public boolean isCanceled() { return false; }
            @Override public void cancel() {}
        };
    }
}

public class RegistryTest {
    private DummyRegistry registry;
    private Identifier id;

    @BeforeAll
    static void initEventsFacade() throws Exception {
        // Create a dummy IServiceLocator that returns a no-op IEventBus
        IServiceLocator dummyLocator = new IServiceLocator() {
            @Override
            public <T> boolean contains(Class<T> type) { return type == IEventBus.class; }
            @Override
            public <T> Optional<T> get(Class<T> type) {
                if (type == IEventBus.class) {
                    return Optional.of(type.cast(new IEventBus() {
                        public <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener) {}
                        public <T extends IEvent> void unregisterListener(String modId, Class<T> eventType, IEventListener<T> listener) {}
                        public void unregisterMod(String modId) {}
                        public <T extends IEvent> void dispatch(T event) {}
                        public <T extends IEvent> void dispatchAsync(T event) {}
                        public <T extends IEvent> void dispatchOnMainThread(T event) {}
                    }));
                }
                return Optional.empty();
            }
            @Override
            public <T> T require(Class<T> type) {
                if (type == IEventBus.class) {
                    return type.cast(new IEventBus() {
                        public <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener) {}
                        public <T extends IEvent> void unregisterListener(String modId, Class<T> eventType, IEventListener<T> listener) {}
                        public void unregisterMod(String modId) {}
                        public <T extends IEvent> void dispatch(T event) {}
                        public <T extends IEvent> void dispatchAsync(T event) {}
                        public <T extends IEvent> void dispatchOnMainThread(T event) {}
                    });
                }
                throw new RuntimeException("Service not found: " + type);
            }
        };
        // Use reflection to set Events.serviceProvider
        Field f = Class.forName("io.github.hato1883.api.Events").getDeclaredField("serviceProvider");
        f.setAccessible(true);
        f.set(null, dummyLocator);
    }

    @BeforeEach
    void setUp() {
        // Provide a dummy IEventBusService to DummyRegistry
        IEventBusService dummyEventBusService = new IEventBusService() {
            public void shutdown() {}
            public boolean isShutdown() { return false; }
            public <T extends IEvent> void registerListener(String modId, Class<T> eventType, EventPriority priority, IEventListener<T> listener) {}
            public <T extends IEvent> void unregisterListener(String modId, Class<T> eventType, IEventListener<T> listener) {}
            public void unregisterMod(String modId) {}
            public <T extends IEvent> void dispatch(T event) {}
            public <T extends IEvent> void dispatchAsync(T event) {}
            public <T extends IEvent> void dispatchOnMainThread(T event) {}
        };
        registry = new DummyRegistry(dummyEventBusService);
        id = Identifier.of("test:test");
    }

    @Test
    void testRegisterAndReplace() {
        String value = "foo";
        assertEquals(value, registry.register(id, value));
        String newValue = "bar";
        registry.replace(id, newValue);
        // No exception means success
    }

    @Test
    void testRegisterDuplicateThrows() {
        registry.register(id, "foo");
        assertThrows(IllegalArgumentException.class, () -> registry.register(id, "bar"));
    }

    @Test
    void testReplaceNonexistentThrows() {
        assertThrows(IllegalArgumentException.class, () -> registry.replace(id, "bar"));
    }
}
