package io.github.hato1883.api.services;

/**
 * Exception thrown when a requested service cannot be found in the service locator.
 * <p>
 * This exception extends {@link ServiceLocatorException} and is typically thrown when attempting
 * to retrieve a service that hasn't been registered with the {@link IServiceRegistrar}.
 * <p>
 * <b>Common Scenarios:</b>
 * <ul>
 *     <li>Attempting to get a service before it has been registered</li>
 *     <li>Misspelling the service type when retrieving</li>
 *     <li>Attempting to replace or unregister a non-existent service</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 *     try {
 *         IBoardGenerator generator = Services.getService(IBoardGenerator.class);
 *     } catch (ServiceNotFoundException e) {
 *         // Handle case where service isn't registered
 *         Services.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
 *     }
 * }</pre>
 *
 * <h2>Invalid Usage Examples:</h2>
 * <pre>{@code
 *     // Attempting to get an unregistered service without handling the exception
 *     IBoardGenerator generator = Services.getService(IBoardGenerator.class);
 *     // throws ServiceNotFoundException if not registered
 * }</pre>
 *
 * @see IServiceLocator
 * @see ServiceLocatorException
 * @author Hampus Toft
 * @version 1.0.0
 */
public class ServiceNotFoundException extends ServiceLocatorException {
    /**
     * Constructs a new ServiceNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public ServiceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServiceNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
