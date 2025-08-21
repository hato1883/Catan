package io.github.hato1883.api;

import io.github.hato1883.api.services.IServiceContainer;
import io.github.hato1883.api.ui.screen.IScreenManager;
import org.jetbrains.annotations.NotNull;

/**
 * Centralized initializer for all core facades.
 * <p>
 * This class is intended to be used by the core application only. It ensures that
 * all facades are initialized in a controlled, single-call manner. Modders and external
 * code should never call this class directly.
 */
public final class FacadesInitializer {
    private static boolean initialized = false;

    private FacadesInitializer() {
        throw new UnsupportedOperationException("FacadesInitializer is a utility class");
    }

    /**
     * Initializes all core facades. Should be called once during application startup.
     *
     * @param container the service container to use for all facades
     * @throws IllegalStateException if already initialized
     * @throws IllegalArgumentException if container is null
     */
    public static synchronized void initializeServices(@NotNull IServiceContainer container) {
        Services.initialize(validateContainer(container));
    }

    public static synchronized void initializeEvents(@NotNull IServiceContainer container) {
        Events.initialize(validateContainer(container));
    }

    public static synchronized void initializeFactories(@NotNull IServiceContainer container) {
        Factories.initialize(validateContainer(container));
    }

    public static synchronized void initializeRegistries(@NotNull IServiceContainer container) {
        Registries.initialize(validateContainer(container));
    }

    public static synchronized void initializeModLoading(@NotNull IServiceContainer container) {
        ModLoading.initialize(validateContainer(container));
    }

    public static synchronized void initializeScreens(@NotNull IServiceContainer container) {
        Screens.initialize(container.require(IScreenManager.class));
    }

    public static synchronized void initializeAll(@NotNull IServiceContainer container) {
        if (initialized) {
            throw new IllegalStateException("Facades have already been initialized");
        }
        IServiceContainer validated = validateContainer(container);
        initializeServices(validated);
        initializeEvents(validated);
        initializeFactories(validated);
        initializeRegistries(validated);
        initializeModLoading(validated);
        // Add other facade initializations here as needed
        initialized = true;
    }

    public static synchronized void initializeAllUIFacades(@NotNull IServiceContainer container) {
        initializeScreens(container);
        // Add other UI facade initializations here as needed
    }

    /**
     * Validates that the provided service container is not null.
     *
     * @param container the container to validate
     * @return the validated container
     * @throws IllegalArgumentException if the container is null
     */
    private static synchronized IServiceContainer validateContainer(IServiceContainer container) {
        if (container == null) throw new IllegalArgumentException("Service container cannot be null");
        return container;
    }

    /**
     * Returns whether the facades have been initialized.
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
