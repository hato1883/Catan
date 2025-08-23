package io.github.hato1883.core;

import io.github.hato1883.api.ModLoading;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.mod.load.dependency.ModWithPath;
import io.github.hato1883.api.services.IServiceLocator;
import org.junit.jupiter.api.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ModLoadingIntegrationTest {
    private static class TestServiceLocator implements IServiceLocator {
        private final Map<Class<?>, Object> services = new HashMap<>();
        <T> void register(Class<T> type, T instance) { services.put(type, instance); }
        @Override public <T> boolean contains(Class<T> type) { return services.containsKey(type); }
        @Override public <T> Optional<T> get(Class<T> type) { return Optional.ofNullable(type.cast(services.get(type))); }
        @Override public <T> T require(Class<T> type) { Object o = services.get(type); if (o == null) throw new IllegalStateException("Service not found: " + type); return type.cast(o); }
    }
    TestServiceLocator locator;
    IModDiscovery discovery;
    IDependencyResolver dependencyResolver;
    IModMetadataReader metadataReader;
    IModClassLoaderFactory classLoaderFactory;
    IModListenerScanner listenerScanner;
    IRegistryLoader registryLoader;
    IModAssetLoader modAssetLoader;
    IModInitializer initializer;

    @BeforeEach
    void setUp() {
        locator = new TestServiceLocator();
        discovery = new TestModDiscovery();
        dependencyResolver = new TestDependencyResolver();
        metadataReader = new TestMetadataReader();
        classLoaderFactory = new TestClassLoaderFactory();
        listenerScanner = new TestListenerScanner();
        registryLoader = new TestRegistryLoader();
        modAssetLoader = new TestAssetLoader();
        initializer = new TestInitializer();

        locator.register(IModDiscovery.class, discovery);
        locator.register(IDependencyResolver.class, dependencyResolver);
        locator.register(IModMetadataReader.class, metadataReader);
        locator.register(IModClassLoaderFactory.class, classLoaderFactory);
        locator.register(IModListenerScanner.class, listenerScanner);
        locator.register(IRegistryLoader.class, registryLoader);
        locator.register(IModAssetLoader.class, modAssetLoader);
        locator.register(IModInitializer.class, initializer);
    }

    @AfterEach
    void tearDown() throws Exception {
        var serviceProviderField = ModLoading.class.getDeclaredField("serviceProvider");
        serviceProviderField.setAccessible(true);
        serviceProviderField.set(null, null);
        for (String field : new String[]{"discovery","dependencyResolver","metadataReader","classLoaderFactory","listenerScanner","listenerRegistrar","registryLoader","modAssetLoader","initializer"}) {
            var f = ModLoading.class.getDeclaredField(field);
            f.setAccessible(true);
            f.set(null, null);
        }
    }

    @Test
    void testModLoadingReturnsRegisteredServices() {
        ModLoading.builder().withServiceLocator(locator).build();
        assertSame(discovery, ModLoading.discovery());
        assertSame(dependencyResolver, ModLoading.dependencyResolver());
        assertSame(metadataReader, ModLoading.metadataReader());
        assertSame(classLoaderFactory, ModLoading.classLoaderFactory());
        assertSame(listenerScanner, ModLoading.listenerScanner());
        assertSame(registryLoader, ModLoading.registryLoader());
        assertSame(modAssetLoader, ModLoading.modAssetLoader());
        assertSame(initializer, ModLoading.initializer());
    }

    @Test
    void testThrowsIfNotInitialized() {
        Exception ex = assertThrows(IllegalStateException.class, ModLoading::discovery);
        assertTrue(ex.getMessage().contains("not been initialized"));
    }

    @Test
    void testThrowsIfDoubleInitialize() {
        ModLoading.builder().withServiceLocator(locator).build();
        Exception ex = assertThrows(IllegalStateException.class, () ->
            ModLoading.builder().withServiceLocator(locator).build());
        assertTrue(ex.getMessage().contains("already initialized"));
    }

    // Minimal stub implementations for all required interfaces
    static class TestModDiscovery implements IModDiscovery {
        @Override
        public List<Path> discoverMods(Path modsDir) { return List.of(); }
    }
    static class TestDependencyResolver implements IDependencyResolver {
        @Override
        public List<ModWithPath> resolveLoadOrder(Map<ModMetadata, Path> mods) {
            List<ModWithPath> result = new java.util.ArrayList<>();
            for (Map.Entry<ModMetadata, Path> entry : mods.entrySet()) {
                result.add(new ModWithPath(entry.getKey(), entry.getValue()));
            }
            return result;
        }
    }
    static class TestMetadataReader implements IModMetadataReader {
        @Override
        public ModMetadata readMetadata(Path path) { return null; }
    }
    static class TestClassLoaderFactory implements IModClassLoaderFactory {
        @Override
        public ClassLoader createClassLoader(Path path) { return getClass().getClassLoader(); }
    }
    static class TestListenerScanner implements IModListenerScanner {
        @Override
        public void scanAndRegister(List<ILoadedMod> mods) {}
    }
    static class TestRegistryLoader implements IRegistryLoader {
        @Override
        public void loadRegistries(List<ILoadedMod> mods) {}
    }
    static class TestAssetLoader implements IModAssetLoader {
        @Override
        public void loadAssets(List<ILoadedMod> mods) {}
    }
    static class TestInitializer implements IModInitializer {
        @Override
        public void initializeAll(List<ILoadedMod> mods) {}
    }
}
