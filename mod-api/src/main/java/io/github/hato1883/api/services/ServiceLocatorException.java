package io.github.hato1883.api.services;

/**
 * Base exception class for all service locator related exceptions.
 * <p>
 * This abstract class serves as the foundation for specific exceptions that may occur
 * during service registration, retrieval, or management operations in the {@link IServiceLocator} or {@link IServiceRegistrar}.
 * It follows the standard exception pattern with both message-only and cause-including constructors.
 * <p>
 * <b>Common Scenarios:</b>
 * <ul>
 *     <li>Attempting to register a service that already exists</li>
 *     <li>Trying to retrieve a service that hasn't been registered</li>
 *     <li>Passing null arguments to service locator methods</li>
 *     <li>Other service-related runtime errors</li>
 * </ul>
 *
 * <h2>Example Usage in ServiceLocator:</h2>
 * <pre>{@code
 *     if (serviceMap.containsKey(type)) {
 *         throw new ServiceAlreadyRegisteredException(
 *             "Service already registered for type: " + type.getName()
 *         );
 *     }
 * }</pre>
 *
 * <h2>Example Exception Handling:</h2>
 * <pre>{@code
 *     try {
 *         ServiceLocator.registerService(IBoardGenerator.class, generator);
 *     } catch (ServiceLocatorException ex) {
 *         logger.error("Failed to register service", ex);
 *         throw new InitializationException("Service initialization failed", ex);
 *     }
 * }</pre>
 *
 * @author Hampus Toft
 * @version 1.0.0
 */
public abstract class ServiceLocatorException extends RuntimeException {
    /**
     * Constructs a new service locator exception with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    ServiceLocatorException(String message) {
        super(message);
    }

    /**
     * Constructs a new service locator exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    ServiceLocatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
