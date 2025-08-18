package io.github.hato1883.api.service;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public interface IServiceLocator {
    /**
     * Registers a service instance under a given type.
     *
     * <p><b>Important:</b> A service can only be registered once. Attempting to
     * register another instance of the same type will throw an exception.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * IBoardGenerator generator = new DefaultBoardGenerator();
     * ServiceLocator.registerService(IBoardGenerator.class, generator);
     * }</pre>
     *
     * <p><b>Invalid Example (duplicate registration):</b></p>
     * <pre>{@code
     * ServiceLocator.registerService(IBoardGenerator.class, new CustomBoardGenerator());
     * // throws ServiceLocatorException: Service already registered
     * }</pre>
     *
     * @param type the class type to register the service under
     * @param instance the service instance
     * @param <T> the type of the service
     * @throws ServiceLocatorException if type or instance is null, or if the service is already registered
     */
    <T> void registerService(@NotNull Class<T> type, @NotNull T instance);
    <T> void registerServiceIfAbsent(@NotNull Class<T> type, @NotNull T instance);

    /**
     * Registers a service instance under a given type.
     *
     * <p><b>Important:</b> A service can only be registered once. Attempting to
     * register another instance of the same type will throw an exception.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * IBoardGenerator generator = new DefaultBoardGenerator();
     * ServiceLocator.registerService(IBoardGenerator.class, generator);
     * }</pre>
     *
     * <p><b>Invalid Example (duplicate registration):</b></p>
     * <pre>{@code
     * ServiceLocator.registerService(IBoardGenerator.class, new CustomBoardGenerator());
     * // throws ServiceLocatorException: Service already registered
     * }</pre>
     *
     * @param type the class type to register the service under
     * @param supplier the lazy supplier of service instance
     * @param <T> the type of the service
     * @throws ServiceLocatorException if type or instance is null, or if the service is already registered
     */
    <T> void registerService(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier);
    <T> void registerServiceIfAbsent(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier);

    /**
     * Replaces an existing service with a new instance.
     *
     * <p><b>Important:</b> Protected services cannot be replaced. Replacing
     * an unregistered service will also throw an exception.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * ServiceLocator.replaceService(IBoardGenerator.class, new CustomBoardGenerator());
     * }</pre>
     *
     * <p><b>Invalid Example (protected service):</b></p>
     * <pre>{@code
     * ServiceLocator.replaceService(IRegistry.class, new CustomRegistry());
     * // throws ServiceLocatorException: Protected service cannot be removed or modified
     * }</pre>
     *
     * <p><b>Invalid Example (unregistered service):</b></p>
     * <pre>{@code
     * ServiceLocator.replaceService(UnregisteredService.class, new Implementation());
     * // throws ServiceLocatorException: Cannot replace unregistered service
     * }</pre>
     *
     * @param type the service type
     * @param instance the new service instance
     * @param <T> the type of the service
     * @throws ServiceLocatorException if type or instance is null, service is protected, or service is not registered
     */
    <T> void replaceService(@NotNull Class<T> type, @NotNull T instance);

    /**
     * Replaces an existing service with a new instance.
     *
     * <p><b>Important:</b> Protected services cannot be replaced. Replacing
     * an unregistered service will also throw an exception.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * ServiceLocator.replaceService(IBoardGenerator.class, new CustomBoardGenerator());
     * }</pre>
     *
     * <p><b>Invalid Example (protected service):</b></p>
     * <pre>{@code
     * ServiceLocator.replaceService(IRegistry.class, new CustomRegistry());
     * // throws ServiceLocatorException: Protected service cannot be removed or modified
     * }</pre>
     *
     * <p><b>Invalid Example (unregistered service):</b></p>
     * <pre>{@code
     * ServiceLocator.replaceService(UnregisteredService.class, new Implementation());
     * // throws ServiceLocatorException: Cannot replace unregistered service
     * }</pre>
     *
     * @param type the service type
     * @param supplier the lazy supplier of service instance
     * @param <T> the type of the service
     * @throws ServiceLocatorException if type or instance is null, service is protected, or service is not registered
     */
    <T> void replaceService(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier);

    /**
     * Checks if a service is already registered under the given type.
     *
     * <p>This method returns {@code true} if there is either a supplier or an instance
     * registered for the specified service type, regardless of whether the service
     * has been initialized yet. It does not trigger lazy initialization of a supplier.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * if (ServiceLocator.contains(IBoardGenerator.class)) {
     *     IBoardGenerator generator = ServiceLocator.getService(IBoardGenerator.class);
     * }
     * }</pre>
     *
     * @param type the service type to check
     * @return true if a service (supplier or instance) is registered for this type, false otherwise
     * @throws ServiceLocatorException if type is null
     */
    <T> boolean contains(@NotNull Class<T> type);

    /**
     * Unregisters a service from the locator.
     *
     * <p><b>Important:</b> Protected services cannot be unregistered.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * ServiceLocator.unregisterService(CustomService.class);
     * }</pre>
     *
     * <p><b>Invalid Example (protected service):</b></p>
     * <pre>{@code
     * ServiceLocator.unregisterService(ModLoadingService.class);
     * // throws ServiceLocatorException: Protected service cannot be removed or modified
     * }</pre>
     *
     * @param type the service type
     * @param <T> the type of the service
     * @throws ServiceLocatorException if the service is protected
     */
    <T> void unregisterService(@NotNull Class<T> type);

    /**
     * Retrieves a registered service by its type.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * IBoardGenerator generator = ServiceLocator.getService(IBoardGenerator.class);
     * }</pre>
     *
     * <p><b>Invalid Example (service not registered):</b></p>
     * <pre>{@code
     * ServiceLocator.getService(UnregisteredService.class);
     * // throws ServiceLocatorException: No service registered for UnregisteredService
     * }</pre>
     *
     * @param type the service type
     * @param <T> the type of the service
     * @return the service instance, or Empty Optional if no such service exists
     */
    <T> Optional<T> getService(@NotNull Class<T> type);

    /**
     * Retrieves a registered service by its type.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * IBoardGenerator generator = ServiceLocator.getService(IBoardGenerator.class);
     * }</pre>
     *
     * <p><b>Invalid Example (service not registered):</b></p>
     * <pre>{@code
     * ServiceLocator.getService(UnregisteredService.class);
     * // throws ServiceLocatorException: No service registered for UnregisteredService
     * }</pre>
     *
     * @param type the service type
     * @param <T> the type of the service
     * @return the service instance
     * @throws ServiceLocatorException if no service is registered for the type
     */
    <T> T requireService(@NotNull Class<T> type);
}
