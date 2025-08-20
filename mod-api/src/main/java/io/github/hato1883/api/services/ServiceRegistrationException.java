package io.github.hato1883.api.services;

/**
 * Thrown to indicate that a service registration operation has failed.
 * <p>
 * This exception is used as a wrapper for various registration-related errors that may occur
 * when attempting to register multiple different services. Instead of rethrowing the original
 * exceptions directly, they are wrapped with this exception type to provide consistent error
 * handling while preserving the original cause through the exception chain.
 * <p>
 * <b>Usage Example:</b>
 * <pre>{@code
 *     try {
 *         module.registerServices(registrar);
 *     } catch (ServiceLocatorException | IllegalArgumentException exception) {
 *         throw new ServiceRegistrationException(
 *             "Failed to register services from module: " + module.getModuleName(),
 *             exception
 *         );
 *     }
 * }</pre>
 *
 * <p><b>Invalid Usage Example:</b></p>
 * <pre>{@code
 *     // Attempting to register duplicate service, and masking cause of failure
 *     try {
 *         ServiceLocator.registerService(IBoardGenerator.class, generator1);
 *         ServiceLocator.registerService(IBoardGenerator.class, generator2);
 *     } catch (ServiceLocatorException exception) {
 *         throw new ServiceRegistrationException("Failed to register services: IBoardGenerator");
 *     }
 * }</pre>
 *
 * @author Hampus Toft
 * @version 1.0.0
 */
public class ServiceRegistrationException extends RuntimeException {
    /**
     * Constructs a new ServiceRegistrationException with the specified detail message.
     *
     * @param message the detail message describing the registration failure
     */
    public ServiceRegistrationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServiceRegistrationException with the specified detail message and cause.
     * <p>
     * This constructor is typically used when wrapping another exception that occurred during
     * the service registration process.
     *
     * @param message the detail message describing the registration failure
     * @param cause the original exception that caused the registration to fail
     */
    public ServiceRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
