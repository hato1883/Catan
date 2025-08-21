package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.ModLoading;
import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.services.IServiceLocator;
import org.junit.jupiter.api.*;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ModLoader} covering mod loading, dependency resolution, and error handling.
 * <p>
 * These tests verify that the mod loading process works as expected, including:
 * <ul>
 *   <li>Successful loading of mods and correct metadata association.</li>
 *   <li>Proper handling of dependency resolution failures (edge case: dependency errors).</li>
 *   <li>Graceful skipping of mods when metadata reading fails (edge case: metadata errors).</li>
 * </ul>
 * The test doubles (fakes) are used to simulate various scenarios and edge cases.
 */
class ModLoaderTest {
    static class FakeServiceLocator implements IServiceLocator {
        private final Map<Class<?>, Object> services = new HashMap<>();
        <T> void register(@NotNull Class<T> type, @NotNull T instance) { services.put(type, instance); }
        @Override public <T> boolean contains(@NotNull Class<T> type) { return services.containsKey(type); }
        @Override public <T> Optional<T> get(@NotNull Class<T> type) { return Optional.ofNullable(type.cast(services.get(type))); }
        @Override public <T> T require(@NotNull Class<T> type) { Object o = services.get(type); if (o == null) throw new IllegalStateException("Service not found: " + type); return type.cast(o); }
    }

    FakeServiceLocator locator;
    TestModDiscovery discovery;
    TestDependencyResolver dependencyResolver;
    TestMetadataReader metadataReader;
    TestClassLoaderFactory classLoaderFactory;
    TestListenerScanner listenerScanner;
    TestRegistryLoader registryLoader;
    TestAssetLoader modAssetLoader;
    TestInitializer initializer;

    @BeforeEach
    void setUp() {
        locator = new FakeServiceLocator();
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

        ModLoading.builder().withServiceLocator(locator).build();
    }

    @AfterEach
    void tearDown() {
        ModLoading.reset();
    }

    /**
     * Tests that {@link ModLoader#loadAll(Path)} successfully loads mods when all dependencies are resolved
     * and metadata is valid. Verifies that the loaded mod's metadata matches expectations.
     * <p>
     * Edge case: Only one mod is present and all collaborators behave as expected.
     */
    @Test
    @DisplayName("Should load all mods successfully when dependencies and metadata are valid")
    void testLoadAll_successful() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path path = Path.of("/fake/mods/mod1");
        discovery.mods = List.of(path);
        ModMetadata meta = new ModMetadata(
            "mod1", "mod1", "1.0.0", TestCatanMod.class.getName(), "desc",
            List.of(), io.github.hato1883.api.mod.load.LoadPriority.NORMAL
        );
        metadataReader.meta = meta;
        dependencyResolver.loadOrder = Map.of(meta, path);
        classLoaderFactory.modInstance = new TestCatanMod();

        // Assert that ModLoading.discovery() returns our test double
        assertSame(discovery, ModLoading.discovery(), "ModLoading.discovery() is not the test double!");

        ModLoader loader = new ModLoader();
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(1, loaded.size());
        assertEquals(meta, loaded.getFirst().metadata());
    }

    /**
     * Tests that {@link ModLoader#loadAll(Path)} throws a {@link ModLoadingException} when dependency
     * resolution fails. This simulates the edge case where mods have unsatisfied or cyclic dependencies.
     * <p>
     * Edge case: Dependency resolver throws an exception, which should be wrapped in ModLoadingException.
     */
    @Test
    @DisplayName("Should throw ModLoadingException if dependency resolution fails")
    void testLoadAll_dependencyResolutionFails() {
        Path fakeModsDir = Path.of("/fake/mods");
        discovery.mods = List.of(Path.of("/fake/mods/mod1"));
        metadataReader.meta = new ModMetadata(
            "mod1", "mod1", "1.0.0", "TestCatanMod", "desc",
            List.of(), LoadPriority.NORMAL
        );
        dependencyResolver.shouldThrow = true;

        ModLoader loader = new ModLoader();
        ModLoadingException ex = assertThrows(ModLoadingException.class, () -> loader.loadAll(fakeModsDir));
        assertTrue(ex.getMessage().contains("dependency issues"));
    }

    /**
     * Tests that {@link ModLoader#loadAll(Path)} skips mods whose metadata cannot be read, instead of failing
     * the entire loading process. This ensures robustness when encountering corrupt or unreadable mod metadata.
     * <p>
     * Edge case: Metadata reader throws an exception, but loading continues for other mods.
     */
    @Test
    @DisplayName("Should skip mods whose metadata cannot be read and continue loading others")
    void testLoadAll_metadataReadFails() {
        Path fakeModsDir = Path.of("/fake/mods");
        discovery.mods = List.of(Path.of("/fake/mods/mod1"));
        metadataReader.shouldThrow = true;

        ModLoader loader = new ModLoader();
        // Should not throw, but should skip the mod (no loaded mods)
        List<ILoadedMod> loaded = assertDoesNotThrow(() -> loader.loadAll(fakeModsDir));
        assertEquals(0, loaded.size());
    }

    // --- Test stubs ---
    static class TestModDiscovery implements IModDiscovery {
        List<Path> mods = List.of();
        boolean called = false;
        @Override
        public List<Path> discoverMods(Path modsDir) {
            called = true;
            // System.out.println("Discovered mods: " + discovered);
            return mods;
        }
    }
    static class TestDependencyResolver implements IDependencyResolver {
        Map<ModMetadata, Path> loadOrder = Map.of();
        boolean shouldThrow = false;
        boolean called = false;
        @Override
        public Map<ModMetadata, Path> resolveLoadOrder(Map<ModMetadata, Path> mods) {
            called = true;
            // System.out.println("TestDependencyResolver.resolveLoadOrder called");
            if (shouldThrow) throw new RuntimeException("Dependency error");
            return loadOrder;
        }
    }
    static class TestMetadataReader implements IModMetadataReader {
        ModMetadata meta;
        boolean shouldThrow = false;
        boolean called = false;
        @Override
        public ModMetadata readMetadata(Path path) {
            called = true;
            // System.out.println("Metadata read: " + metaRead);
            if (shouldThrow) throw new RuntimeException("Metadata error");
            return meta;
        }
    }
    static class TestClassLoaderFactory implements IModClassLoaderFactory {
        CatanMod modInstance;
        @Override
        public ClassLoader createClassLoader(Path path) { return getClass().getClassLoader(); }
    }
    static class TestListenerScanner implements IModListenerScanner {
        @Override public void scanAndRegister(List<ILoadedMod> mods) {}
    }
    static class TestRegistryLoader implements IRegistryLoader {
        @Override public void loadRegistries(List<ILoadedMod> mods) {}
    }
    static class TestAssetLoader implements IModAssetLoader {
        @Override public void loadAssets(List<ILoadedMod> mods) {}
    }
    static class TestInitializer implements IModInitializer {
        @Override public void initializeAll(List<ILoadedMod> mods) {}
    }
    static class TestCatanMod implements CatanMod {
        @Override public void onInitialize() {}
    }
}
