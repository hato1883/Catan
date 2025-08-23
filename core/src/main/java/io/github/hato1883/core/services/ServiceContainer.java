package io.github.hato1883.core.services;

import io.github.hato1883.api.events.IEventBus;
import io.github.hato1883.api.events.IEventBusService;
import io.github.hato1883.api.services.*;
import io.github.hato1883.api.world.board.IBoardGenerator;
import io.github.hato1883.api.registries.IRegistry;
import io.github.hato1883.core.modloading.loading.ModLoader;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * ServiceContainer is a central registry for managing singleton services in the game.
 * <p>
 * It allows registration, retrieval, replacement, and conditional registration of services
 * using their interface or class type as a key. This enables decoupling of concrete
 * implementations from code that depends on the service, following the Dependency Inversion
 * and Open/Closed principles.
 * <p>
 * <b>Key Principles:</b>
 * <ul>
 *     <li>Single Responsibility: Only manages services.</li>
 *     <li>DRY: Shared validation for type/instance checks.</li>
 *     <li>Thread-safe: Uses ConcurrentHashMap internally.</li>
 * </ul>
 *
 * <h2>Valid Usage Examples:</h2>
 * <pre>{@code
 * // Register a new service
 * ServiceContainer.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
 *
 * // Retrieve a service
 * IBoardGenerator generator = ServiceContainer.getService(IBoardGenerator.class);
 *
 * // Replace an existing service
 * ServiceContainer.replaceService(IBoardGenerator.class, new CustomBoardGenerator());
 *
 * // Register if absent (will not overwrite)
 * ServiceContainer.registerIfAbsent(IBoardGenerator.class, new AnotherBoardGenerator());
 *
 * // Unregister a service
 * ServiceContainer.unregisterService(IBoardGenerator.class);
 * }</pre>
 *
 * <h2>Invalid Usage Examples (will throw {@link ServiceLocatorException}):</h2>
 * <pre>{@code
 * // Attempt to register null type
 * ServiceContainer.registerService(null, new DefaultBoardGenerator());
 *
 * // Attempt to register null instance
 * ServiceContainer.registerService(IBoardGenerator.class, null);
 *
 * // Attempt to register service when one already exists
 * ServiceContainer.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
 *
 * // Attempt to replace a service that does not exist
 * ServiceContainer.replaceService(ITileTypeRegistry.class, new CustomTileRegistry());
 *
 * // Attempt to get a service that has not been registered
 * ServiceContainer.getService(IBuildingTypeRegistry.class);
 * }</pre>
 *
 * <h2>Expected Services for Modders:</h2>
 * <p>These are services that modders can safely register or replace to extend game functionality:</p>
 * <ul>
 *     <li>{@link IBoardGenerator} - Controls board generation</li>
 * </ul>
 *
 * <p><b>Restricted Services:</b> The following are internal and should not be replaced by mods:</p>
 * <ul>
 *     <li>{@link IRegistry} - Provides tile types for the game</li>
 *     <li>{@link IEventBus} and {@link IEventBusService}- Dispatches events to all registered listeners</li>
 *     <li>{@link ModLoader} - Mod loading logic</li>
 *     <li>Core event dispatchers that handle critical game lifecycle events</li>
 *     <li>RegistryManager singleton itself</li>
 * </ul>
 *
 * @author Hampus Toft
 * @version 1.0
 */
public final class ServiceContainer implements IServiceContainer {

    private final Map<Class<?>, Supplier<?>> SERVICES = new ConcurrentHashMap<>();

    // Services that cannot be replaced or unregistered
    private final Set<Class<?>> PROTECTED_SERVICES = Set.of(
        IRegistry.class,
        ModLoader.class
    );

    public Map<Class<?>, Supplier<?>> getAll() {
        return Map.copyOf(SERVICES);
    }

    @Override
    public IServiceLocator getLocator() {
        return this;
    }

    @Override
    public IServiceRegistrar getRegistrar() {
        return this;
    }

    public <T> void register(@NotNull Class<T> type, @NotNull T instance) {
        validateTypeAndInstance(type, instance);
        if (SERVICES.containsKey(type)) {
            throw new ServiceAlreadyExistsException("Service already registered for type: " + type.getName());
        }
        SERVICES.put(type, new SingletonSupplier<>(instance));
    }

    public <T> void register(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier) {
        validateTypeAndSupplier(type, supplier);
        if (SERVICES.containsKey(type)) {
            throw new ServiceProtectedException("Service already registered for type: " + type.getName());
        }
        SERVICES.put(type, supplier);
    }

    @Override
    public <T> void registerIfAbsent(@NotNull Class<T> type, @NotNull T instance){
        validateTypeAndInstance(type, instance);
        if (!SERVICES.containsKey(type)) {
            SERVICES.put(type, new SingletonSupplier<>(instance));
        }
    }

    @Override
    public <T> void registerIfAbsent(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier) {
        validateTypeAndSupplier(type, supplier);
        if (!SERVICES.containsKey(type)) {
            SERVICES.put(type, supplier);
        }
    }

    public <T> void replace(@NotNull Class<T> type, @NotNull T instance) {
        validateTypeAndInstance(type, instance);
        if (!SERVICES.containsKey(type)) {
            throw new ServiceNotFoundException("Cannot replace service: no existing service registered for type " + type.getName());
        }
        if (PROTECTED_SERVICES.contains(type)) {
            throw new ServiceProtectedException("Protected service cannot be replaced: " + type.getName());
        }
        SERVICES.put(type, new SingletonSupplier<>(instance));
    }

    public <T> void replace(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier) {
        validateTypeAndSupplier(type, supplier);
        if (!SERVICES.containsKey(type)) {
            throw new ServiceNotFoundException("Cannot replace service: no existing service registered for type " + type.getName());
        }
        if (PROTECTED_SERVICES.contains(type)) {
            throw new ServiceProtectedException("Protected service cannot be replaced: " + type.getName());
        }
        SERVICES.put(type, supplier);
    }

    public <T> boolean contains(@NotNull Class<T> type) {
        validateType(type);
        return SERVICES.containsKey(type);
    }

    public <T> void unregister(@NotNull Class<T> type) {
        if (PROTECTED_SERVICES.contains(type)) {
            throw new ServiceProtectedException("Protected service cannot be removed: " + type.getName());
        }
        SERVICES.remove(type);
    }

    public <T> Optional<T> get(@NotNull Class<T> type) {
        Supplier<?> supplier = SERVICES.get(type);
        if (supplier == null) return Optional.empty();

        synchronized (supplier) {
            // Replace supplier with singleton instance after first creation
            Object instance;
            if (supplier instanceof SingletonSupplier<?>(Object singleton)) {
                instance = singleton;
            } else {
                instance = supplier.get();
                SERVICES.put(type, new SingletonSupplier<>(instance));
            }
            return (instance == null) ? Optional.empty() : Optional.of(type.cast(instance));
        }
    }

    public <T> T require(@NotNull Class<T> type) {
        Supplier<?> supplier = SERVICES.get(type);
        if (supplier == null) throw new ServiceNotFoundException("No service registered for type: " + type.getName());

        synchronized (supplier) {
            // Replace supplier with singleton instance after first creation
            Object instance;
            if (supplier instanceof SingletonSupplier<?>(Object singleton)) {
                instance = singleton;
            } else {
                instance = supplier.get();
                SERVICES.put(type, new SingletonSupplier<>(instance));
            }
            if (instance == null) {
                throw new ServiceInstantiationException("Supplier for service " + type.getName() + " returned null");
            }
            return type.cast(instance);
        }
    }

    // --- Private helper ---
    /**
     * Validates that the service type is not null.
     *
     * @param type the service type
     * @throws IllegalArgumentException if type or instance is null
     */
    private <T> void validateType(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Cannot register service: type is null");
        }
    }
    /**
     * Validates that the service instance is not null.
     *
     * @param instance the service instance
     * @throws IllegalArgumentException if type or instance is null
     */
    private <T> void validateInstance(T instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Cannot register service: instance is null");
        }
    }
    /**
     * Validates that the service supplier is not null.
     *
     * @param supplier the lazy supplier of service instance
     * @throws IllegalArgumentException if type or instance is null
     */
    private <T> void validateSupplier(Supplier<? extends T> supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Cannot register service: supplier is null");
        }
    }
    /**
     * Validates that neither the service type nor the instance is null.
     *
     * @param type the service type
     * @param instance the service instance
     * @throws IllegalArgumentException if type or instance is null
     */
    private <T> void validateTypeAndInstance(Class<T> type, T instance) {
        validateType(type);
        validateInstance(instance);
    }
    /**
     * Validates that neither the service type nor the instance supplier is null.
     *
     * @param type the service type
     * @param supplier the lazy supplier of service instance
     * @throws IllegalArgumentException if type or instance is null
     */
    private <T> void validateTypeAndSupplier(Class<T> type, Supplier<? extends T> supplier) {
        validateType(type);
        validateSupplier(supplier);
    }


    // Helper class to wrap singleton instance
    private record SingletonSupplier<T>(T instance) implements Supplier<T> {
        public T get() {
            return instance;
        }
    }
}
