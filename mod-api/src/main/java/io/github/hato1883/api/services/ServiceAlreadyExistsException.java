package io.github.hato1883.api.services;

/**
 * Thrown when an attempt is made to register a service that already exists in the service locator.
 * <p>
 * This exception is typically thrown by {@link IServiceRegistrar} when code attempts to register
 * a service instance for a type that already has a registered service. Services can only be
 * registered once per type to maintain singleton behavior.
 * <p>
 * <b>Example Usage:</b>
 * <pre>{@code
 *     try {
 *         Services.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
 *         // This second registration will throw ServiceAlreadyExistsException
 *         Services.registerService(IBoardGenerator.class, new CustomBoardGenerator());
 *     } catch (ServiceAlreadyExistsException e) {
 *         logger.error("Failed to register service", e);
 *     }
 * }</pre>
 *
 * <p><b>Handling Recommendations:</b></p>
 * <ul>
 *     <li>Use {@link IServiceRegistrar#replace} if you intend to override an existing service</li>
 *     <li>Use {@link IServiceRegistrar#registerIfAbsent} if you want to register only when no service exists</li>
 *     <li>Check {@link IServiceRegistrar#contains} before attempting registration</li>
 * </ul>
 *
 * @author Hampus Toft
 * @version 1.0.0
 * @see IServiceRegistrar
 * @see RuntimeException
 */
public class ServiceAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new ServiceAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message explaining which service type already exists
     */
    public ServiceAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServiceAlreadyExistsException with the specified detail message and cause.
     *
     * @param message the detail message explaining which service type already exists
     * @param cause the underlying cause of this exception
     */
    public ServiceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
