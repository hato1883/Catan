package io.github.hato1883.api.services;

/**
 * Thrown when attempting to replace a protected service that cannot be modified.
 * <p>
 * This exception is used by the service locator system to prevent modification of
 * critical internal services that are required for the game's core functionality.
 * Protected services typically include core systems like event buses, registry
 * managers, and mod loading infrastructure.
 *
 * <h2>Example Scenario:</h2>
 * <pre>{@code
 *     try {
 *         // Attempt to replace a protected service
 *         ServiceLocator.replaceService(IRegistry.class, new CustomRegistry());
 *     } catch (ServiceProtectedException e) {
 *         // Handle the case where the service is protected
 *         logger.error("Cannot replace protected service: " + e.getMessage());
 *     }
 * }</pre>
 *
 * <h2>Protected Services:</h2>
 * <p>The following services are typically protected and will throw this exception if replacement is attempted:</p>
 * <ul>
 *     <li>{@code IRegistry} - Core registry system</li>
 *     <li>{@code IEventBus} - Event dispatch system</li>
 *     <li>{@code ModLoading} - Mod loading infrastructure</li>
 * </ul>
 *
 * @author Hampus Toft
 * @version 1.0.0
 */
public class ServiceProtectedException extends ServiceLocatorException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message describing which protected service was
     *                attempted to be replaced
     */
    public ServiceProtectedException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message describing which protected service was
     *                attempted to be replaced
     * @param cause the underlying cause of this exception
     */
    public ServiceProtectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
