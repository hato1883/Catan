package io.github.hato1883.api.services;

/**
 * Exception thrown when a service cannot be instantiated during registration or retrieval.
 * <p>
 * This exception is typically thrown when:
 * <ul>
 *     <li>A service constructor throws an exception during instantiation</li>
 *     <li>Required dependencies for service instantiation are not available</li>
 *     <li>There are access restrictions preventing instantiation</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 *     try {
 *         ServiceLocator.registerService(IBoardGenerator.class, DefaultBoardGenerator.class);
 *     } catch (ServiceInstantiationException e) {
 *         // Handle failure to instantiate the service
 *         logger.error("Failed to instantiate board generator", e);
 *     }
 * }</pre>
 *
 * <h2>Common Causes:</h2>
 * <pre>{@code
 *     // Service class requires constructor arguments
 *     public class MyService {
 *         public MyService(Dependency dep) { ... }
 *     }
 *
 *     // Service class throws exception in constructor
 *     public class FaultyService {
 *         public FaultyService() throws IOException { ... }
 *     }
 * }</pre>
 *
 * @author Hampus Toft
 * @version 1.0.0
 * @see ServiceLocatorException
 */
public class ServiceInstantiationException extends ServiceLocatorException {
    /**
     * Creates a new ServiceInstantiationException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public ServiceInstantiationException(String message) {
        super(message);
    }

    /**
     * Creates a new ServiceInstantiationException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method)
     */
    public ServiceInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
