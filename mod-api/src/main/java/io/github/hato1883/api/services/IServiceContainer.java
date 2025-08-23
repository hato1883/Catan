package io.github.hato1883.api.services;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A container interface that combines service location and registration capabilities.
 * <p>
 * This interface extends both {@link IServiceLocator} and {@link IServiceRegistrar}, providing
 * a unified interface for both retrieving and managing services. It serves as the central
 * abstraction for service containers in the system.
 * <p>
 * <b>Key Features:</b>
 * <ul>
 *     <li>Combines service lookup and registration in a single interface</li>
 *     <li>Follows the Dependency Inversion Principle by working with interfaces</li>
 *     <li>Provides a consistent API for service management</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 *     // Get a service container instance
 *     IServiceContainer container = ...;
 *
 *     // Register a service
 *     container.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
 *
 *     // Retrieve a service
 *     IBoardGenerator generator = container.getService(IBoardGenerator.class);
 *
 *     // Replace an existing service
 *     container.replaceService(IBoardGenerator.class, new CustomBoardGenerator());
 * }</pre>
 *
 * <h2>See Also:</h2>
 * <ul>
 *     <li>{@link IServiceLocator} - For service retrieval operations</li>
 *     <li>{@link IServiceRegistrar} - For service registration operations</li>
 * </ul>
 *
 * @author Hampus Toft
 * @version 1.0.0
 */
public interface IServiceContainer extends IServiceLocator, IServiceRegistrar {
    Map<Class<?>, Supplier<?>>  getAll();
    IServiceLocator getLocator();
    IServiceRegistrar getRegistrar();
}
