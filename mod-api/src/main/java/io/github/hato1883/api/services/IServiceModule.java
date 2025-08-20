package io.github.hato1883.api.services;

/**
 * Defines a service module that can register services with a service registrar.
 * <p>
 * Service modules are used to organize and bundle related services together, providing
 * a way to modularize service registration in the application. Each module is responsible
 * for registering its own set of services when requested.
 * <p>
 * <b>Key Principles:</b>
 * <ul>
 *     <li>Modularity: Services are grouped logically by module.</li>
 *     <li>Single Responsibility: Each module only registers its own services.</li>
 *     <li>Discoverability: Modules can be identified by their name.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * public class BoardGenerationModule implements IServiceModule {
 *     public void registerServices(IServiceRegistrar registrar) {
 *         registrar.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
 *         registrar.registerService(IBoardValidator.class, new StandardBoardValidator());
 *     }
 *
 *     public String getModuleName() {
 *         return "BoardGeneration";
 *     }
 * }
 * }</pre>
 *
 * @author Hampus Toft
 * @version 1.0.0
 * @see IServiceRegistrar
 */
public interface IServiceModule {
    /**
     * Registers all services provided by this module with the given registrar.
     * <p>
     * Implementations should register their services by calling appropriate methods
     * on the provided registrar instance. The registrar handles duplicate registration
     * checks and null validation.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     *     public void registerServices(IServiceRegistrar registrar) {
     *         registrar.registerService(IBoardGenerator.class, new DefaultBoardGenerator());
     *     }
     * }</pre>
     *
     * @param container the service Container capable of registering services and also retrieving existing services with
     * @throws IllegalArgumentException if registrar is null
     */
    void registerServices(IServiceContainer container);

    /**
     * Returns the name of this service module.
     * <p>
     * The module name should be unique within the application and clearly identify
     * the purpose or domain of the services provided by this module.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * public String getModuleName() {
     *     return "BoardGeneration";
     * }
     * }</pre>
     *
     * @return the name of this module, never null
     */
    String getModuleName();
}
