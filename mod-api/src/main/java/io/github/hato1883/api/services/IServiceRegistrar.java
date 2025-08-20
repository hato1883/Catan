package io.github.hato1883.api.services;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The {@code IServiceRegistrar} interface defines operations for managing service registrations
 * in a service locator pattern implementation. It provides methods for registering, replacing,
 * and unregistering services with support for both direct instances and lazy initialization
 * via {@link Supplier}.
 * <p>
 * <b>Key Features:</b>
 * <ul>
 *     <li>Support for both eager and lazy service registration</li>
 *     <li>Conditional registration that preserves existing services</li>
 *     <li>Explicit service replacement operations</li>
 *     <li>Type-safe operations using generics</li>
 *     <li>Null-safety through {@code @NotNull} annotations</li>
 * </ul>
 *
 * <h2>Usage Patterns:</h2>
 * <pre>{@code
 *     // Register an immediate instance
 *     registrar.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
 *
 *     // Register a lazy instance (only created when first requested)
 *     registrar.registerService(IBoardGenerator.class, () -> new HeavyBoardGenerator());
 *
 *     // Register only if no existing service exists
 *     registrar.registerServiceIfAbsent(IBoardGenerator.class, new FallbackGenerator());
 *
 *     // Forcefully replace an existing service
 *     registrar.replaceService(IBoardGenerator.class, new CustomBoardGenerator());
 *
 *     // Remove a service registration
 *     registrar.unregisterService(IBoardGenerator.class);
 * }</pre>
 *
 * @author Hampus Toft
 * @version 1.0.0
 * @see Supplier
 * @see IServiceLocator
 */
public interface IServiceRegistrar {
    /**
     * Checks whether the service locator contains a registration for the specified type.
     *
     * <p><b>Note:</b> This method should return {@code false} if the type is not registered,
     * rather than throwing an exception. This makes it suitable for pre-checking before
     * calling {@code requireService()}.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     *     if (registrar.contains(IBoardGenerator.class)) {
     *         // Safe to call replaceService() without getting a exception
     *         registrar.replaceService(IBoardGenerator.class, new BoardGenerator());
     *     }
     *
     *     if (!registrar.contains(IBoardGenerator.class)) {
     *         // Safe to call registerService() without getting a exception
     *         registrar.registerService(IBoardGenerator.class, new BoardGenerator());
     *     }
     * }</pre>
     *
     * @param type the class type to check for registration
     * @param <T> the type of the service
     * @return {@code true} if the service is registered, {@code false} otherwise
     */
    <T> boolean contains(@NotNull Class<T> type);

    /**
     * Registers a service instance under the specified type.
     * <p>
     * The registration will fail if:
     * <ul>
     *     <li>The service type is already registered</li>
     *     <li>Either parameter is null</li>
     * </ul>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     registrar.registerService(ICoolService.class, new CoolService());
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     registrar.registerService(null, new CoolService());
     *
     *     // Null instance
     *     registrar.registerService(ICoolService.class, null);
     * }</pre>
     *
     * @apiNote Do not use {@code IServiceLocator#getService().isEmpty()} as an existence check It can report Empty
     * Optionals even though a supplier is registered, if the given supplier itself is faulty and is returning null.
     *
     * @param <T> the type of the service to register
     * @param type the class object representing the service type
     * @param instance the service instance to register
     * @throws ServiceAlreadyExistsException if a service of the given type is already registered
     * @throws IllegalArgumentException if either parameter is null
     */
    <T> void register(@NotNull Class<T> type, @NotNull T instance);

    /**
     * Registers a service instance only if no service of the specified type is currently registered.
     * <p>
     * This is a non-destructive operation that preserves existing registrations. If a service
     * of the given type already exists, this method returns silently without changing the registration.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     registrar.registerServiceIfAbsent(ICoolService.class, new CoolService());
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     registrar.registerServiceIfAbsent(null, new CoolService());
     *
     *     // Null instance
     *     registrar.registerServiceIfAbsent(ICoolService.class, null);
     * }</pre>
     *
     * @param <T> the type of the service to register
     * @param type the class object representing the service type
     * @param instance the service instance to register
     * @throws IllegalArgumentException  if either parameter is null
     */
    <T> void registerIfAbsent(@NotNull Class<T> type, @NotNull T instance);

    /**
     * Registers a service supplier that will be invoked to create the service instance when first needed.
     * <p>
     * The supplier will be called at most once, when the service is first requested. Subsequent
     * requests will receive the same instance.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     registrar.registerService(IHeavyService.class,
     *         () -> new HeavyService(expensiveConfig));
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     registrar.registerService(null,
     *         () -> new HeavyService(expensiveConfig));
     *
     *     // Supplier that returns null, Will throw exception when requested
     *     registrar.registerService(IHeavyService.class, () -> null);
     *
     *     // Null supplier
     *     registrar.registerService(IHeavyService.class, null);
     * }</pre>
     *
     * @apiNote Do not use {@code IServiceLocator#getService().isEmpty()} as an existence check It can report Empty
     * Optionals even though a supplier is registered, if the given supplier itself is faulty and is returning null.
     *
     * @param <T> the type of the service to register
     * @param type the class object representing the service type
     * @param supplier the supplier that will provide the service instance when needed
     * @throws ServiceAlreadyExistsException if a service of the given type is already registered
     * @throws IllegalArgumentException if either parameter is null
     */
    <T> void register(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier);

    /**
     * Registers a service supplier only if no service of the specified type is currently registered.
     * <p>
     * The supplier will not be invoked if the service is never requested. If the service type
     * is already registered, this method returns silently without changing the registration.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     registrar.registerServiceIfAbsent(IHeavyService.class,
     *         () -> new HeavyService(expensiveConfig));
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     registrar.registerServiceIfAbsent(null,
     *         () -> new HeavyService(expensiveConfig));
     *
     *     // Supplier that returns null, Will throw exception when requested
     *     registrar.registerServiceIfAbsent(IHeavyService.class, () -> null);
     *
     *     // Null supplier
     *     registrar.registerServiceIfAbsent(IHeavyService.class, null);
     * }</pre>
     *
     * @param <T> the type of the service to register
     * @param type the class object representing the service type
     * @param supplier the supplier that will provide the service instance when needed
     * @throws IllegalArgumentException if either parameter is null
     */
    <T> void registerIfAbsent(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier);

    /**
     * Replaces an existing service registration with a new instance.
     * <p>
     * This operation will fail if no service of the specified type is currently registered.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     // Making sure a ICoolService is registered
     *     registrar.registerServiceIfAbsent(ICoolService.class, new CoolService());
     *
     *     // A ICoolService Service has been registered before this call
     *     registrar.replaceService(ICoolService.class, new NewCoolerService());
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     registrar.replaceService(null, new CoolService());
     *
     *     // Null instance
     *     registrar.replaceService(ICoolService.class, null);
     *
     *     // A ICoolService Service has NOT been registered before this call
     *     registrar.replaceService(ICoolService.class, new CoolService());
     * }</pre>
     *
     * @param <T> the type of the service to replace
     * @param type the class object representing the service type
     * @param instance the new service instance
     * @throws ServiceNotFoundException if no service of the given type is registered
     * @throws ServiceProtectedException if trying to replace a critical service
     * @throws IllegalArgumentException if either parameter is null
     */
    <T> void replace(@NotNull Class<T> type, @NotNull T instance);

    /**
     * Replaces an existing service registration with a new supplier.
     * <p>
     * The supplier will be invoked when the service is next requested. This operation
     * will fail if no service of the specified type is currently registered.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     // Making sure a ICoolService is registered
     *     registrar.registerServiceIfAbsent(ICoolService.class, () -> new CoolService());
     *
     *     // A ICoolService Service has been registered before this call
     *     registrar.replaceService(ICoolService.class, () -> new NewCoolerService());
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     registrar.replaceService(null, () -> new CoolService());
     *
     *     // Null Supplier
     *     registrar.replaceService(ICoolService.class, null);
     *
     *     // Supplier that returns null, Will throw exception when requested
     *     registrar.replaceService(ICoolService.class, () -> null);
     *
     *     // A ICoolService Service has NOT been registered before this call
     *     registrar.replaceService(ICoolService.class, () -> new CoolService());
     * }</pre>
     *
     * @param <T> the type of the service to replace
     * @param type the class object representing the service type
     * @param supplier the new supplier that will provide the service instance
     * @throws ServiceNotFoundException if no service of the given type is registered
     * @throws ServiceProtectedException if trying to replace a critical service
     * @throws IllegalArgumentException if either parameter is null
     */
    <T> void replace(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier);

    /**
     * Removes the service registration for the specified type.
     * <p>
     * If no service of the given type is registered, this method returns silently.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     // Making sure a ICoolService is registered
     *     registrar.registerServiceIfAbsent(ICoolService.class, () -> new CoolService());
     *
     *     // A ICoolService Service has been registered before this call
     *     registrar.unregisterService(ICoolService.class);
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     registrar.unregisterService(null);
     * }</pre>
     *
     * @param <T> the type of the service to unregister
     * @param type the class object representing the service type
     * @throws IllegalArgumentException if the type parameter is null
     */
    <T> void unregister(@NotNull Class<T> type);
}
