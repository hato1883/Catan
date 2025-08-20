package io.github.hato1883.api.services;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * The {@code IServiceLocator} interface defines the contract for a service locator pattern implementation,
 * providing methods to query and retrieve services by their type.
 * <p>
 * This interface enables decoupling service consumers from concrete implementations by allowing them to
 * request services through their interface or class type. Implementations should follow these principles:
 * <ul>
 *     <li><b>Type Safety:</b> All methods are generic and checked at compile time</li>
 *     <li><b>Null Safety:</b> Parameters are annotated as {@code @NotNull} and implementations should reject null types</li>
 *     <li><b>Consistent Behavior:</b> Implementations should clearly document their behavior for missing services</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 *     // Check if a service exists
 *     if (serviceLocator.contains(IBoardGenerator.class)) {
 *         // Get an optional service
 *         Optional<IBoardGenerator> generator = serviceLocator.getService(IBoardGenerator.class);
 *
 *         // Require a service (throws if not found)
 *         IBoardGenerator requiredGenerator = serviceLocator.requireService(IBoardGenerator.class);
 *     }
 * }</pre>
 *
 * <h2>Implementation Requirements:</h2>
 * <ul>
 *     <li>Implementations must throw an {@link ServiceNotFoundException} when {@code requireService()} cannot find a service</li>
 *     <li>Implementations should return {@code false} for {@code contains()} when given a null type</li>
 *     <li>Implementations should return {@code Optional.empty()} for {@code getService()} when given a null type</li>
 * </ul>
 *
 * @author Hampus Toft
 * @version 1.0.0
 * @see Optional
 * @see IServiceRegistrar
 */
public interface IServiceLocator {
    /**
     * Checks whether the service locator contains a registration for the specified type.
     *
     * <p><b>Note:</b> This method should return {@code false} if the type is not registered,
     * rather than throwing an exception. This makes it suitable for pre-checking before
     * calling {@code requireService()}.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     *     if (locator.contains(IBoardGenerator.class)) {
     *         // Safe to call requireService() without getting a exception
     *         IBoardGenerator generator = locator.requireService(IBoardGenerator.class);
     *     }
     * }</pre>
     *
     * @param type the class type to check for registration
     * @param <T> the type of the service
     * @return {@code true} if the service is registered, {@code false} otherwise
     */
    <T> boolean contains(@NotNull Class<T> type);

    /**
     * Retrieves an optional service instance of the specified type.
     *
     * <p><b>Behavior:</b></p>
     * <ul>
     *     <li>Returns {@code Optional.empty()} if the service is not registered</li>
     *     <li>Returns a present {@code Optional} if the service is registered</li>
     *     <li>Should not throw exceptions for missing services</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     *     Optional<IBoardGenerator> generator = locator.getService(IBoardGenerator.class);
     *     generator.ifPresent(g -> g.generateBoard());
     * }</pre>
     *
     * @param type the class type of the service to retrieve
     * @param <T> the type of the service
     * @return an {@code Optional} containing the service instance if found, otherwise empty
     */
    <T> Optional<T> get(@NotNull Class<T> type);

    /**
     * Retrieves a service instance of the specified type, throwing an unchecked exception if not found.
     *
     * <p><b>Important:</b> This method should only be used when the caller is certain the service
     * exists, or when the absence of the service represents a fatal error condition.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * try {
     *     IBoardGenerator generator = locator.requireService(IBoardGenerator.class);
     *     generator.generateBoard();
     * } catch (ServiceNotFoundException e) {
     *     // Handle missing required service
     * }
     * }</pre>
     *
     * @param type the class type of the service to retrieve
     * @param <T> the type of the service
     * @return the service instance (never null)
     * @throws ServiceNotFoundException if the service is not registered
     * @throws ServiceInstantiationException if a lazy supplier returned a null value
     */
    <T> T require(@NotNull Class<T> type);
}
