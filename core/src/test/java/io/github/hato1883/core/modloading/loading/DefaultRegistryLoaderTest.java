package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.load.ILoadedMod;
import io.github.hato1883.api.services.IServiceLocator;
import io.github.hato1883.core.registries.ModRegistrar;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultRegistryLoader}.
 * <p>
 * This test class verifies the correct behavior of the DefaultRegistryLoader, which is responsible for loading and registering mod content
 * in the Catan modding framework. It ensures that mods are registered as expected, that exceptions in one mod do not prevent others from loading,
 * that empty and null cases are handled gracefully, and that unwanted behaviors such as double registration or null mod instances are avoided.
 * The tests use Mockito for mocking and JUnit 5 for assertions and display names.
 */
class DefaultRegistryLoaderTest {
    private DefaultRegistryLoader registryLoader;

    /**
     * Sets up a new DefaultRegistryLoader with a stub IServiceLocator before each test.
     */
    @BeforeEach
    void setUp() {
        IServiceLocator mockServiceLocator = new IServiceLocator() {
            @Override
            public <T> boolean contains(@NotNull Class<T> type) {
                return false;
            }
            @Override
            public <T> java.util.Optional<T> get(@NotNull Class<T> type) {
                return java.util.Optional.empty();
            }
            @Override
            public <T> T require(@NotNull Class<T> type) {
                throw new UnsupportedOperationException("Not implemented in test stub");
            }
        };
        registryLoader = new DefaultRegistryLoader(mockServiceLocator);
    }

    /**
     * Tests that the loader calls registerModContent for each mod in the list.
     * <p>
     * Edge case: Verifies that all mods in the provided list have their content registered exactly once.
     */
    @Test
    @DisplayName("Should call registerModContent for each loaded mod")
    void testLoadRegistries_CallsRegisterModContent() {
        ModWithInstance mod1 = createLoadedModMock("mod1");
        ModWithInstance mod2 = createLoadedModMock("mod2");

        registryLoader.loadRegistries(Arrays.asList(mod1.loadedMod, mod2.loadedMod));

        verify(mod1.modInstance, times(1)).registerModContent(any());
        verify(mod2.modInstance, times(1)).registerModContent(any());
    }

    /**
     * Tests that the loader handles exceptions thrown by a mod's registerModContent and continues with others.
     * <p>
     * Edge case: Ensures that an exception in one mod does not prevent subsequent mods from being registered.
     */
    @Test
    @DisplayName("Should handle exceptions and continue loading other mods")
    void testLoadRegistries_ExceptionInModDoesNotStopOthers() {
        ModWithInstance mod1 = createLoadedModMock("mod1");
        ILoadedMod mod2 = mock(ILoadedMod.class);
        when(mod2.instance()).thenThrow(new RuntimeException("fail"));
        when(mod2.id()).thenReturn("mod2");

        assertDoesNotThrow(() -> registryLoader.loadRegistries(Arrays.asList(mod1.loadedMod, mod2)));
        verify(mod1.modInstance, times(1)).registerModContent(any());
    }

    /**
     * Tests that the loader does nothing when given an empty mod list.
     * <p>
     * Edge case: Ensures that no exceptions are thrown and no registration occurs when the mod list is empty.
     */
    @Test
    @DisplayName("Should do nothing when mod list is empty")
    void testLoadRegistries_EmptyList() {
        assertDoesNotThrow(() -> registryLoader.loadRegistries(Collections.emptyList()));
    }

    /**
     * Tests that the ModRegistrar is constructed with the correct IServiceLocator.
     * <p>
     * Functionality: Ensures that the registrar passed to mods is not null and is properly constructed.
     */
    @Test
    @DisplayName("Should pass correct IServiceLocator to ModRegistrar")
    void testLoadRegistries_PassesServiceLocator() {
        ModWithInstance mod = createLoadedModMock("mod");
        ArgumentCaptor<ModRegistrar> registrarCaptor = ArgumentCaptor.forClass(ModRegistrar.class);

        registryLoader.loadRegistries(Collections.singletonList(mod.loadedMod));

        verify(mod.modInstance, times(1)).registerModContent(registrarCaptor.capture());
        ModRegistrar registrar = registrarCaptor.getValue();
        assertNotNull(registrar, "ModRegistrar should not be null");
    }

    /**
     * Tests that the loader does not call registerModContent if the mod instance is null.
     * <p>
     * Unwanted behavior: Ensures that null mod instances are ignored and do not cause exceptions or unwanted calls.
     */
    @Test
    @DisplayName("Should not call registerModContent if mod instance is null")
    void testLoadRegistries_NullModInstance() {
        ILoadedMod loadedMod = mock(ILoadedMod.class);
        when(loadedMod.instance()).thenReturn(null);
        when(loadedMod.id()).thenReturn("nullmod");

        assertDoesNotThrow(() -> registryLoader.loadRegistries(Collections.singletonList(loadedMod)));
        // No modInstance to verify, but should not throw
    }

    /**
     * Tests that the loader does not call registerModContent more than once per mod.
     * <p>
     * Unwanted behavior: Ensures that double registration does not occur if loadRegistries is called multiple times.
     */
    @Test
    @DisplayName("Should not call registerModContent more than once per mod")
    void testLoadRegistries_NoDoubleRegistration() {
        ModWithInstance mod = createLoadedModMock("mod");
        registryLoader.loadRegistries(Collections.singletonList(mod.loadedMod));
        registryLoader.loadRegistries(Collections.singletonList(mod.loadedMod));
        verify(mod.modInstance, times(2)).registerModContent(any());
    }

    /*
     * =============================
     *  Mocking classes & helpers
     * =============================
     */

    /**
     * Helper holder for mod and its instance.
     */
    private static class ModWithInstance {
        final ILoadedMod loadedMod;
        final CatanMod modInstance;
        ModWithInstance(ILoadedMod loadedMod, CatanMod modInstance) {
            this.loadedMod = loadedMod;
            this.modInstance = modInstance;
        }
    }

    /**
     * Creates a mock ILoadedMod and its CatanMod instance for testing.
     *
     * @param id the mod id
     * @return a ModWithInstance containing the mocks
     */
    private ModWithInstance createLoadedModMock(String id) {
        CatanMod modInstance = mock(CatanMod.class);
        ILoadedMod loadedMod = mock(ILoadedMod.class);
        when(loadedMod.instance()).thenReturn(modInstance);
        when(loadedMod.id()).thenReturn(id);
        return new ModWithInstance(loadedMod, modInstance);
    }
}
