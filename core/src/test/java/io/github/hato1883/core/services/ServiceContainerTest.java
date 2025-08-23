package io.github.hato1883.core.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceContainerTest {
    interface DummyService {}
    static class DummyServiceImpl implements DummyService {}
    static class AnotherDummyServiceImpl implements DummyService {}

    private ServiceContainer serviceContainer;

    @BeforeEach
    void setUp() {
        serviceContainer = new ServiceContainer();
    }

    @Test
    void testRegisterAndGetService() {
        DummyService service = new DummyServiceImpl();
        serviceContainer.register(DummyService.class, service);
        assertTrue(serviceContainer.get(DummyService.class).isPresent());
        assertEquals(service, serviceContainer.get(DummyService.class).get());
    }

    @Test
    void testReplaceService() {
        DummyService service1 = new DummyServiceImpl();
        DummyService service2 = new AnotherDummyServiceImpl();
        serviceContainer.register(DummyService.class, service1);
        serviceContainer.replace(DummyService.class, service2);
        assertEquals(service2, serviceContainer.get(DummyService.class).get());
    }

    @Test
    void testUnregisterService() {
        DummyService service = new DummyServiceImpl();
        serviceContainer.register(DummyService.class, service);
        serviceContainer.unregister(DummyService.class);
        assertTrue(serviceContainer.get(DummyService.class).isEmpty());
    }
}
