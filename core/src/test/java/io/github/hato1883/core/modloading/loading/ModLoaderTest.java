package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.asset.IModAssetLoader;
import io.github.hato1883.api.mod.load.dependency.IDependencyResolver;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.ModWithPath;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;
import io.github.hato1883.api.services.IServiceLocator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ModLoader}.
 * <p>
 * This class tests the mod loading process, including:
 * <ul>
 *   <li>Successful mod loading and correct metadata association</li>
 *   <li>Dependency resolution failures and error propagation</li>
 *   <li>Graceful skipping of mods with unreadable metadata</li>
 *   <li>Class loader failures and unwanted mod loading</li>
 *   <li>Asset, registry, and listener loading/initialization</li>
 *   <li>Prevention of duplicate mod loading</li>
 *   <li>Robustness against exceptions in asset/registry/initializer</li>
 * </ul>
 * Test doubles are used to simulate edge cases and verify both wanted and unwanted behaviors.
 */
@ExtendWith(MockitoExtension.class)
class ModLoaderTest {
    static class FakeServiceLocator implements IServiceLocator {
        private final Map<Class<?>, Object> services = new HashMap<>();
        <T> void register(@NotNull Class<T> type, @NotNull T instance) { services.put(type, instance); }
        @Override public <T> boolean contains(@NotNull Class<T> type) { return services.containsKey(type); }
        @Override public <T> Optional<T> get(@NotNull Class<T> type) { return Optional.ofNullable(type.cast(services.get(type))); }
        @Override public <T> T require(@NotNull Class<T> type) { Object o = services.get(type); if (o == null) throw new IllegalStateException("Service not found: " + type); return type.cast(o); }
    }

    FakeServiceLocator locator;
    @Mock IModDiscovery discovery;
    @Mock IDependencyResolver dependencyResolver;
    @Mock IModMetadataReader metadataReader;
    @Mock IModClassLoaderFactory classLoaderFactory;
    @Mock IModListenerScanner listenerScanner;
    @Mock IRegistryLoader registryLoader;
    @Mock IModAssetLoader modAssetLoader;
    @Mock IModInitializer initializer;

    @BeforeEach
    void setUp() {
        locator = new FakeServiceLocator();
        locator.register(IModDiscovery.class, discovery);
        locator.register(IDependencyResolver.class, dependencyResolver);
        locator.register(IModMetadataReader.class, metadataReader);
        locator.register(IModClassLoaderFactory.class, classLoaderFactory);
        locator.register(IModListenerScanner.class, listenerScanner);
        locator.register(IRegistryLoader.class, registryLoader);
        locator.register(IModAssetLoader.class, modAssetLoader);
        locator.register(IModInitializer.class, initializer);
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
        ModMetadata meta = new ModMetadata(
            "mod1", "mod1", "1.0.0", TestCatanMod.class.getName(), "desc",
            List.of(), LoadPriority.NORMAL
        );
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(path));
        Mockito.when(metadataReader.readMetadata(path)).thenReturn(meta);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new io.github.hato1883.api.mod.load.dependency.ModWithPath(meta, path)));
        Mockito.when(classLoaderFactory.createClassLoader(path)).thenReturn(getClass().getClassLoader());

        ModLoader loader = new ModLoader(
            discovery,
            dependencyResolver,
            metadataReader,
            classLoaderFactory,
            listenerScanner,
            registryLoader,
            modAssetLoader,
            initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(1, loaded.size());
        assertEquals("mod1", loaded.getFirst().metadata().id());
    }

    /**
     * Tests that {@link ModLoader#loadAll(Path)} throws a {@link ModLoadingException} when dependency
     * resolution fails. This simulates the edge case where mods have unsatisfied or cyclic dependencies.
     * <p>
     * Edge case: Dependency resolver throws an exception, which should be wrapped in ModLoadingException.
     */
    @Test
    @DisplayName("Should throw ModLoadingException if dependency resolution fails")
    void testLoadAll_dependencyResolutionFails() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path path = Path.of("/fake/mods/mod1");
        ModMetadata meta = new ModMetadata(
            "mod1", "mod1", "1.0.0", "TestCatanMod", "desc",
            List.of(), LoadPriority.NORMAL
        );
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(path));
        Mockito.when(metadataReader.readMetadata(path)).thenReturn(meta);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap())).thenThrow(new RuntimeException("Dependency error"));

        ModLoader loader = new ModLoader(
            discovery,
            dependencyResolver,
            metadataReader,
            classLoaderFactory,
            listenerScanner,
            registryLoader,
            modAssetLoader,
            initializer
        );
        ModLoadingException ex = assertThrows(ModLoadingException.class, () -> loader.loadAll(fakeModsDir));
        assertTrue(ex.getMessage().contains("dependency"));
    }

    /**
     * Tests that {@link ModLoader#loadAll(Path)} skips mods whose metadata cannot be read, instead of failing
     * the entire loading process. This ensures robustness when encountering corrupt or unreadable mod metadata.
     * <p>
     * Edge case: Metadata reader throws an exception, but loading continues for other mods.
     */
    @Test
    @DisplayName("Should skip mods whose metadata cannot be read and continue loading others")
    void testLoadAll_metadataReadFails() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path path = Path.of("/fake/mods/mod1");
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(path));
        Mockito.when(metadataReader.readMetadata(path)).thenThrow(new RuntimeException("Metadata error"));

        ModLoader loader = new ModLoader(
            discovery,
            dependencyResolver,
            metadataReader,
            classLoaderFactory,
            listenerScanner,
            registryLoader,
            modAssetLoader,
            initializer
        );
        List<ILoadedMod> loaded = assertDoesNotThrow(() -> loader.loadAll(fakeModsDir));
        assertEquals(0, loaded.size());
    }

    /**
     * Tests that mods are not loaded if the class loader fails to instantiate the mod class.
     * <p>
     * Edge case: The class loader throws an exception, and the mod is skipped.
     */
    @Test
    @DisplayName("Should skip mods if class loader fails to instantiate mod class")
    void testLoadAll_classLoaderFails() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path path = Path.of("/fake/mods/mod1");
        ModMetadata meta = new ModMetadata(
            "mod1", "mod1", "1.0.0", TestCatanMod.class.getName(), "desc",
            List.of(), LoadPriority.NORMAL
        );
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(path));
        Mockito.when(metadataReader.readMetadata(path)).thenReturn(meta);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new io.github.hato1883.api.mod.load.dependency.ModWithPath(meta, path)));
        Mockito.when(classLoaderFactory.createClassLoader(path)).thenThrow(new RuntimeException("Class loader error"));

        ModLoader loader = new ModLoader(
            discovery,
            dependencyResolver,
            metadataReader,
            classLoaderFactory,
            listenerScanner,
            registryLoader,
            modAssetLoader,
            initializer
        );
        List<ILoadedMod> loaded = assertDoesNotThrow(() -> loader.loadAll(fakeModsDir));
        assertEquals(0, loaded.size());
    }

    /**
     * Tests that asset loader, registry loader, listener scanner, and initializer are called for loaded mods.
     * <p>
     * Verifies that all post-loading steps are performed.
     */
    @Test
    @DisplayName("Should call asset loader, registry loader, listener scanner, and initializer for loaded mods")
    void testLoadAll_callsAllCollaborators() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path path = Path.of("/fake/mods/mod1");
        ModMetadata meta = new ModMetadata(
            "mod1", "mod1", "1.0.0", TestCatanMod.class.getName(), "desc",
            List.of(), LoadPriority.NORMAL
        );
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(path));
        Mockito.when(metadataReader.readMetadata(path)).thenReturn(meta);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new io.github.hato1883.api.mod.load.dependency.ModWithPath(meta, path)));
        Mockito.when(classLoaderFactory.createClassLoader(path)).thenReturn(getClass().getClassLoader());

        ModLoader loader = new ModLoader(
            discovery,
            dependencyResolver,
            metadataReader,
            classLoaderFactory,
            listenerScanner,
            registryLoader,
            modAssetLoader,
            initializer
        );
        loader.loadAll(fakeModsDir);
        // TODO: add Listener to modloading
        // Mockito.verify(listenerScanner).scanAndRegister(Mockito.anyList());
        Mockito.verify(registryLoader).loadRegistries(Mockito.anyList());
        Mockito.verify(modAssetLoader).loadAssets(Mockito.anyList());
        Mockito.verify(initializer).initializeAll(Mockito.anyList());
    }

    /**
     * Tests that duplicate mods (same mod id) are not loaded more than once, and only the highest version is loaded.
     * <p>
     * Edge case: Two mods with the same id but different versions are discovered; only the highest version is loaded and an error is logged for the inferior version.
     */
    @Test
    @DisplayName("Should not load duplicate mods with the same mod id, only highest version")
    void testLoadAll_duplicateMods() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path path1 = Path.of("/fake/mods/mod1");
        Path path2 = Path.of("/fake/mods/mod1-duplicate");
        ModMetadata meta1 = new ModMetadata(
            "mod1", "mod1", "1.0.0", TestCatanMod.class.getName(), "desc",
            List.of(), LoadPriority.NORMAL
        );
        ModMetadata meta2 = new ModMetadata(
            "mod1", "mod1", "2.0.0", TestCatanMod.class.getName(), "desc",
            List.of(), LoadPriority.NORMAL
        );
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(path1, path2));
        Mockito.when(metadataReader.readMetadata(path1)).thenReturn(meta1);
        Mockito.when(metadataReader.readMetadata(path2)).thenReturn(meta2);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(meta2, path2)));
        Mockito.when(classLoaderFactory.createClassLoader(Mockito.any())).thenReturn(getClass().getClassLoader());

        ModLoader loader = new ModLoader(
            discovery,
            dependencyResolver,
            metadataReader,
            classLoaderFactory,
            listenerScanner,
            registryLoader,
            modAssetLoader,
            initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(1, loaded.size());
        assertEquals("2.0.0", loaded.getFirst().metadata().version());
    }

    /**
     * Tests that exceptions in asset loader, registry loader, or initializer do not crash the loader.
     * <p>
     * Edge case: Asset/registry/initializer throw exceptions, but mods are still returned.
     */
    @Test
    @DisplayName("Should not crash if asset/registry/initializer throw exceptions")
    void testLoadAll_assetRegistryInitializerThrow() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path path = Path.of("/fake/mods/mod1");
        ModMetadata meta = new ModMetadata(
            "mod1", "mod1", "1.0.0", TestCatanMod.class.getName(), "desc",
            List.of(), LoadPriority.NORMAL
        );
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(path));
        Mockito.when(metadataReader.readMetadata(path)).thenReturn(meta);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(meta, path)));
        Mockito.when(classLoaderFactory.createClassLoader(path)).thenReturn(getClass().getClassLoader());
        Mockito.doThrow(new RuntimeException("Registry error")).when(registryLoader).loadRegistries(Mockito.anyList());

        ModLoader loader = new ModLoader(
            discovery,
            dependencyResolver,
            metadataReader,
            classLoaderFactory,
            listenerScanner,
            registryLoader,
            modAssetLoader,
            initializer
        );
        List<ILoadedMod> loaded = assertDoesNotThrow(() -> loader.loadAll(fakeModsDir));
        assertEquals(0, loaded.size());
    }

    /**
     * Tests that if a mod fails during registry loading, it and its hard dependents are removed from loaded.
     */
    @Test
    @DisplayName("Should remove failing mod and hard dependents during registry loading")
    void testRegistryFailureRemovesDependents() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path pathA = Path.of("/fake/mods/modA");
        Path pathB = Path.of("/fake/mods/modB");
        ModMetadata metaA = new ModMetadata("modA", "modA", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        ModMetadata metaB = new ModMetadata("modB", "modB", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(new ModDependency("modA", VersionConstraint.any(), false)), LoadPriority.NORMAL);
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(pathA, pathB));
        Mockito.when(metadataReader.readMetadata(pathA)).thenReturn(metaA);
        Mockito.when(metadataReader.readMetadata(pathB)).thenReturn(metaB);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(metaA, pathA), new ModWithPath(metaB, pathB)));
        Mockito.when(classLoaderFactory.createClassLoader(Mockito.any())).thenReturn(getClass().getClassLoader());
        Mockito.doAnswer(inv -> { List<ILoadedMod> mods = inv.getArgument(0); if (mods.getFirst().metadata().id().equals("modA")) throw new RuntimeException("Registry fail"); return null; }).when(registryLoader).loadRegistries(Mockito.anyList());

        ModLoader loader = new ModLoader(
            discovery, dependencyResolver, metadataReader, classLoaderFactory,
            listenerScanner, registryLoader, modAssetLoader, initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(0, loaded.size(), "Both modA and its dependent modB should be removed");
    }

    /**
     * Tests that if a mod fails during asset loading, it and its hard dependents are removed from loaded.
     */
    @Test
    @DisplayName("Should remove failing mod and hard dependents during asset loading")
    void testAssetFailureRemovesDependents() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path pathA = Path.of("/fake/mods/modA");
        Path pathB = Path.of("/fake/mods/modB");
        ModMetadata metaA = new ModMetadata("modA", "modA", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        ModMetadata metaB = new ModMetadata("modB", "modB", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(new ModDependency("modA", VersionConstraint.any(), false)), LoadPriority.NORMAL);
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(pathA, pathB));
        Mockito.when(metadataReader.readMetadata(pathA)).thenReturn(metaA);
        Mockito.when(metadataReader.readMetadata(pathB)).thenReturn(metaB);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(metaA, pathA), new ModWithPath(metaB, pathB)));
        Mockito.when(classLoaderFactory.createClassLoader(Mockito.any())).thenReturn(getClass().getClassLoader());
        Mockito.doAnswer(inv -> { List<ILoadedMod> mods = inv.getArgument(0); if (mods.getFirst().metadata().id().equals("modA")) throw new RuntimeException("Asset fail"); return null; }).when(modAssetLoader).loadAssets(Mockito.anyList());

        ModLoader loader = new ModLoader(
            discovery, dependencyResolver, metadataReader, classLoaderFactory,
            listenerScanner, registryLoader, modAssetLoader, initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(0, loaded.size(), "Both modA and its dependent modB should be removed");
    }

    /**
     * Tests that if a mod fails during initialization, it and its hard dependents are removed from loaded.
     */
    @Test
    @DisplayName("Should remove failing mod and hard dependents during initialization")
    void testInitFailureRemovesDependents() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path pathA = Path.of("/fake/mods/modA");
        Path pathB = Path.of("/fake/mods/modB");
        ModMetadata metaA = new ModMetadata("modA", "modA", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        ModMetadata metaB = new ModMetadata("modB", "modB", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(new ModDependency("modA", VersionConstraint.any(), false)), LoadPriority.NORMAL);
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(pathA, pathB));
        Mockito.when(metadataReader.readMetadata(pathA)).thenReturn(metaA);
        Mockito.when(metadataReader.readMetadata(pathB)).thenReturn(metaB);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(metaA, pathA), new ModWithPath(metaB, pathB)));
        Mockito.when(classLoaderFactory.createClassLoader(Mockito.any())).thenReturn(getClass().getClassLoader());
        Mockito.doAnswer(inv -> { List<ILoadedMod> mods = inv.getArgument(0); if (mods.getFirst().metadata().id().equals("modA")) throw new RuntimeException("Init fail"); return null; }).when(initializer).initializeAll(Mockito.anyList());

        ModLoader loader = new ModLoader(
            discovery, dependencyResolver, metadataReader, classLoaderFactory,
            listenerScanner, registryLoader, modAssetLoader, initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(0, loaded.size(), "Both modA and its dependent modB should be removed");
    }

    /**
     * Tests that if a mod fails during instantiation, it and its hard dependents are removed from loaded.
     */
    @Test
    @DisplayName("Should remove failing mod and hard dependents during instantiation")
    void testInstantiationFailureRemovesDependents() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path pathA = Path.of("/fake/mods/modA");
        Path pathB = Path.of("/fake/mods/modB");
        ModMetadata metaA = new ModMetadata("modA", "modA", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        ModMetadata metaB = new ModMetadata("modB", "modB", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(new ModDependency("modA", VersionConstraint.any(), false)), LoadPriority.NORMAL);
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(pathA, pathB));
        Mockito.when(metadataReader.readMetadata(pathA)).thenReturn(metaA);
        Mockito.when(metadataReader.readMetadata(pathB)).thenReturn(metaB);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(metaA, pathA), new ModWithPath(metaB, pathB)));
        Mockito.when(classLoaderFactory.createClassLoader(pathA)).thenThrow(new RuntimeException("ClassLoader fail"));

        ModLoader loader = new ModLoader(
            discovery, dependencyResolver, metadataReader, classLoaderFactory,
            listenerScanner, registryLoader, modAssetLoader, initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(0, loaded.size(), "Both modA and its dependent modB should be removed");
    }

    /**
     * Tests that a 3-layer dependency tree is removed recursively if the root mod fails.
     * <p>
     * Structure:
     *   modA <- modB (hard) <- modC (hard)
     * If modA fails, modB and modC should also be removed.
     */
    @Test
    @DisplayName("Should recursively remove 3-layer dependency tree if root fails")
    void testRecursiveDependencyRemoval() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path pathA = Path.of("/fake/mods/modA");
        Path pathB = Path.of("/fake/mods/modB");
        Path pathC = Path.of("/fake/mods/modC");
        ModMetadata metaA = new ModMetadata("modA", "modA", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        ModMetadata metaB = new ModMetadata("modB", "modB", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(new ModDependency("modA", VersionConstraint.any(), false)), LoadPriority.NORMAL);
        ModMetadata metaC = new ModMetadata("modC", "modC", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(new ModDependency("modB", VersionConstraint.any(), false)), LoadPriority.NORMAL);
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(pathA, pathB, pathC));
        Mockito.when(metadataReader.readMetadata(pathA)).thenReturn(metaA);
        Mockito.when(metadataReader.readMetadata(pathB)).thenReturn(metaB);
        Mockito.when(metadataReader.readMetadata(pathC)).thenReturn(metaC);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(metaA, pathA), new ModWithPath(metaB, pathB), new ModWithPath(metaC, pathC)));
        Mockito.when(classLoaderFactory.createClassLoader(pathA)).thenThrow(new RuntimeException("ClassLoader fail"));

        ModLoader loader = new ModLoader(
            discovery, dependencyResolver, metadataReader, classLoaderFactory,
            listenerScanner, registryLoader, modAssetLoader, initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(0, loaded.size(), "All mods in the dependency tree should be removed");
    }

    /**
     * Tests that if a mod fails, an independent mod is still loaded.
     */
    @Test
    @DisplayName("Should still load independent mod if another mod fails")
    void testIndependentModSurvivesFailure() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path pathA = Path.of("/fake/mods/modA");
        Path pathB = Path.of("/fake/mods/modB");
        ModMetadata metaA = new ModMetadata("modA", "modA", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        ModMetadata metaB = new ModMetadata("modB", "modB", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(pathA, pathB));
        Mockito.when(metadataReader.readMetadata(pathA)).thenReturn(metaA);
        Mockito.when(metadataReader.readMetadata(pathB)).thenReturn(metaB);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(metaA, pathA), new ModWithPath(metaB, pathB)));
        Mockito.when(classLoaderFactory.createClassLoader(pathA)).thenThrow(new RuntimeException("ClassLoader fail"));
        Mockito.when(classLoaderFactory.createClassLoader(pathB)).thenReturn(getClass().getClassLoader());

        ModLoader loader = new ModLoader(
            discovery, dependencyResolver, metadataReader, classLoaderFactory,
            listenerScanner, registryLoader, modAssetLoader, initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(1, loaded.size(), "Independent modB should still be loaded");
        assertEquals("modB", loaded.getFirst().metadata().id());
    }

    /**
     * Tests that if a mod with an optional dependency fails, the optionally dependent mod is still loaded.
     */
    @Test
    @DisplayName("Should load mod with optional dependency even if dependency fails")
    void testOptionalDependencySurvivesFailure() throws Exception {
        Path fakeModsDir = Path.of("/fake/mods");
        Path pathA = Path.of("/fake/mods/modA");
        Path pathB = Path.of("/fake/mods/modB");
        ModMetadata metaA = new ModMetadata("modA", "modA", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(), LoadPriority.NORMAL);
        ModMetadata metaB = new ModMetadata("modB", "modB", "1.0.0", TestCatanMod.class.getName(), "desc", List.of(new ModDependency("modA", VersionConstraint.any(), true)), LoadPriority.NORMAL);
        Mockito.when(discovery.discoverMods(fakeModsDir)).thenReturn(List.of(pathA, pathB));
        Mockito.when(metadataReader.readMetadata(pathA)).thenReturn(metaA);
        Mockito.when(metadataReader.readMetadata(pathB)).thenReturn(metaB);
        Mockito.when(dependencyResolver.resolveLoadOrder(Mockito.anyMap()))
            .thenReturn(List.of(new ModWithPath(metaA, pathA), new ModWithPath(metaB, pathB)));
        Mockito.when(classLoaderFactory.createClassLoader(pathA)).thenThrow(new RuntimeException("ClassLoader fail"));
        Mockito.when(classLoaderFactory.createClassLoader(pathB)).thenReturn(getClass().getClassLoader());

        ModLoader loader = new ModLoader(
            discovery, dependencyResolver, metadataReader, classLoaderFactory,
            listenerScanner, registryLoader, modAssetLoader, initializer
        );
        List<ILoadedMod> loaded = loader.loadAll(fakeModsDir);
        assertEquals(1, loaded.size(), "ModB should be loaded even if its optional dependency modA fails");
        assertEquals("modB", loaded.getFirst().metadata().id());
    }

    static class TestCatanMod implements CatanMod {
        @Override public void onInitialize() {}
    }
}
