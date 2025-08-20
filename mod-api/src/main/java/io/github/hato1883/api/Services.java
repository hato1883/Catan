package io.github.hato1883.api;

import io.github.hato1883.api.services.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;
/**
 * Services is a facade providing static access to service registration and location functionality.
 * <p>
 * This utility class serves as a centralized entry point for managing services throughout the application,
 * delegating actual implementation to an underlying {@link IServiceContainer}. It follows the Facade pattern
 * to simplify service access while maintaining separation of concerns.
 * <p>
 * <b>Key Features:</b>
 * <ul>
 *     <li>Thread-safe initialization with volatile variables</li>
 *     <li>Delegates to configurable service container implementation</li>
 *     <li>Provides both registration and location capabilities</li>
 *     <li>Supports instance-based and supplier-based service registration</li>
 * </ul>
 *
 * <h2>Initialization Examples:</h2>
 * <pre>{@code
 *     // Initialize during application startup
 *     IServiceContainer container = new SimpleServiceContainer();
 *     Services.initialize(container);
 *
 *     // Replace container for testing
 *     Services.replaceContainer(mockContainer);
 *
 *     // Reset for cleanup (primarily testing)
 *     Services.reset();
 * }</pre>
 *
 * <h2>Service Registration Examples:</h2>
 * <pre>{@code
 *     // Register a service instance
 *     Services.register(IBoardGenerator.class, new DefaultBoardGenerator());
 *
 *     // Register with supplier (lazy initialization)
 *     Services.register(ILogger.class, () -> new FileLogger("app.log"));
 *
 *     // Register only if not already present
 *     Services.registerIfAbsent(IConfigService.class, new ConfigService());
 *
 *     // Replace existing service
 *     Services.replace(IBoardGenerator.class, new AdvancedBoardGenerator());
 *
 *     // Unregister a service
 *     Services.unregister(ILogger.class);
 * }</pre>
 *
 * <h2>Service Location Examples:</h2>
 * <pre>{@code
 *     // Check if service exists
 *     boolean hasLogger = Services.contains(ILogger.class);
 *
 *     // Get service optionally
 *     Optional<ILogger> logger = Services.getService(ILogger.class);
 *
 *     // Require service (throws exception if not found)
 *     IBoardGenerator generator = Services.require(IBoardGenerator.class);
 * }</pre>
 *
 * <h2>Invalid Usage Examples (will throw {@link IllegalStateException}):</h2>
 * <pre>{@code
 *     // Attempt to use before initialization
 *     Services.register(IBoardGenerator.class, new DefaultBoardGenerator());
 *     // throws IllegalStateException: Services facade has not been initialized
 *
 *     // Attempt to initialize multiple times
 *     Services.initialize(container1);
 *     Services.initialize(container2);
 *     // throws IllegalStateException: Services facade is already initialized
 * }</pre>
 *
 * <h2>Invalid Usage Examples (will throw {@link IllegalArgumentException}):</h2>
 * <pre>{@code
 *     // Attempt to initialize with null container
 *     Services.initialize(null);
 *     // throws IllegalArgumentException: Service container cannot be null
 *
 *     // Attempt to register null type
 *     Services.register(null, new DefaultBoardGenerator());
 *     // throws IllegalArgumentException: type is marked non-null but is null
 * }</pre>
 *
 * @author Hampus Toft
 * @version 1.0.0
 */
public final class Services {

    private static volatile IServiceContainer serviceContainer;
    private static volatile boolean initialized = false;

    // Prevent instantiation
    private Services() {
        throw new UnsupportedOperationException("Services is a utility class");
    }

    /**
     * Initialize the Services facade with a ServiceContainer instance.
     *
     * <p><b>Important:</b> This method must be called exactly once during application startup
     * before any service registration or location operations are performed. Subsequent calls
     * will result in an exception.</p>
     *
     * <p><b>Thread Safety:</b> This method uses volatile variables and is thread-safe for
     * initialization scenarios.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     * IServiceContainer container = new SimpleServiceContainer();
     * Services.initialize(container);
     *
     * // Now services can be registered and retrieved
     * Services.register(IBoardGenerator.class, new DefaultBoardGenerator());
     * IBoardGenerator generator = Services.require(IBoardGenerator.class);
     * }</pre>
     *
     * <p><b>Invalid Example (multiple initialization):</b></p>
     * <pre>{@code
     * Services.initialize(container1);
     * Services.initialize(container2);
     * // throws IllegalStateException: Services facade is already initialized
     * }</pre>
     *
     * <p><b>Invalid Example (null container):</b></p>
     * <pre>{@code
     * Services.initialize(null);
     * // throws IllegalArgumentException: Service container cannot be null
     * }</pre>
     *
     * @param container the service container implementation to use for all subsequent operations
     * @throws IllegalStateException if the Services facade has already been initialized
     * @throws IllegalArgumentException if the provided container is null
     *
     * @see #replaceContainer(IServiceContainer)
     * @see #isInitialized()
     * @see #reset()
     */
    public static void initialize(@NotNull IServiceContainer container) {
        if (initialized) {
            throw new IllegalStateException("Services facade is already initialized");
        }
        serviceContainer = validateContainer(container);
        initialized = true;
    }

    /**
     * Replaces the underlying service container with a new instance.
     *
     * <p><b>Important:</b> This method is primarily intended for testing scenarios or
     * hot-swapping implementations at runtime. Use with caution as it immediately
     * affects all subsequent service operations.</p>
     *
     * <p><b>Key Considerations:</b></p>
     * <ul>
     *     <li>Marks the facade as initialized if not already</li>
     *     <li>Immediately replaces all service access points</li>
     *     <li>Does not transfer existing services from the previous container</li>
     *     <li>Thread-safe due to volatile serviceContainer reference</li>
     * </ul>
     *
     * <p><b>Valid Example (testing):</b></p>
     * <pre>{@code
     *     // Setup mock container for unit testing
     *     IServiceContainer mockContainer = mock(IServiceContainer.class);
     *     when(mockContainer.contains(ILogger.class)).thenReturn(true);
     *     when(mockContainer.getService(ILogger.class)).thenReturn(Optional.of(mockLogger));
     *
     *     Services.replaceContainer(mockContainer);
     *
     *     // Now all service operations use the mock container
     *     boolean hasLogger = Services.contains(ILogger.class);
     *     Optional<ILogger> logger = Services.getService(ILogger.class);
     * }</pre>
     *
     * <p><b>Valid Example (runtime swapping):</b></p>
     * <pre>{@code
     *     // Switch to a different container implementation
     *     IServiceContainer newContainer = new AdvancedServiceContainer();
     *     Services.replaceContainer(newContainer);
     *
     *     // Re-register essential services in the new container
     *     Services.register(IConfigService.class, configService);
     *     Services.register(ILogger.class, logger);
     * }</pre>
     *
     * <p><b>Invalid Example (null container):</b></p>
     * <pre>{@code
     *     // Attempt to replace with null container
     *     Services.replaceContainer(null);
     *     // throws IllegalArgumentException: Service container cannot be null
     * }</pre>
     *
     * @param container the new service container to use for all service operations
     * @throws IllegalArgumentException if the provided container is null
     *
     * @see #initialize(IServiceContainer)
     * @see #isInitialized()
     * @see #reset()
     */
    public static void replaceContainer(@NotNull IServiceContainer container) {
        serviceContainer = validateContainer(container);
        initialized = true;
    }

    /**
     * Validates that the provided service container is not null.
     *
     * @param container the container to validate
     * @return the validated container
     * @throws IllegalArgumentException if the container is null
     */
    private static IServiceContainer validateContainer(IServiceContainer container) {
        if (container == null) throw new IllegalArgumentException("Service container cannot be null");
        return container;
    }

    /**
     * Resets the Services facade to its uninitialized state.
     *
     * <p><b>Important:</b> This method is primarily intended for testing purposes and should
     * generally not be used in production code. Resetting the facade while the application
     * is running will cause services to become unavailable to other parts of the system.</p>
     *
     * <p><b>Testing Example:</b></p>
     * <pre>{@code
     *     @Test
     *     public void testServiceRegistration() {
     *         // Setup
     *         IServiceContainer testContainer = new TestServiceContainer();
     *         Services.initialize(testContainer);
     *
     *         // Test service registration
     *         Services.register(IBoardGenerator.class, new MockBoardGenerator());
     *
     *         // Reset for next test
     *         Services.reset();
     *
     *         // Verify reset worked
     *         assertFalse(Services.isInitialized());
     *     }
     * }</pre>
     *
     * <p><b>Invalid Production Usage Example:</b></p>
     * <pre>{@code
     *     public void handleUserRequest() {
     *         // Process request
     *         IBoardGenerator generator = Services.require(IBoardGenerator.class);
     *
     *         // Reset services (DANGEROUS - other requests will fail)
     *         Services.reset();
     *
     *         // Subsequent calls will fail
     *         ILogger logger = Services.require(ILogger.class);
     *         // throws IllegalStateException: Services facade has not been initialized
     *     }
     * }</pre>
     *
     * @see #isInitialized()
     * @see #initialize(IServiceContainer)
     * @see #replaceContainer(IServiceContainer)
     */
    public static void reset() {
        serviceContainer = null;
        initialized = false;
    }

    /**
     * Checks whether the Services facade has been initialized.
     * <p>
     * This method is useful for defensive programming where code needs to verify
     * that the service infrastructure is ready before attempting to use it.
     * </p>
     *
     * <p><b>Typical Usage:</b></p>
     * <pre>{@code
     *     // Check before using services in conditional logic
     *     if (Services.isInitialized()) {
     *         ILogger logger = Services.require(ILogger.class);
     *         logger.info("Services are available");
     *     } else {
     *         System.out.println("Services not initialized yet");
     *     }
     * }</pre>
     *
     * <p><b>Integration with Initialization:</b></p>
     * <pre>{@code
     *     // During application startup sequence
     *     IServiceContainer container = new SimpleServiceContainer();
     *     Services.initialize(container);
     *
     *     // Verify initialization succeeded
     *     if (!Services.isInitialized()) {
     *         throw new RuntimeException("Failed to initialize services");
     *     }
     * }</pre>
     *
     * <p><b>Testing Scenario:</b></p>
     * <pre>{@code
     *     // In unit tests to verify cleanup
     *     @AfterEach
     *     void tearDown() {
     *         Services.reset();
     *         assertFalse(Services.isInitialized(), "Services should be reset after test");
     *     }
     * }</pre>
     *
     * @return {@code true} if the Services facade has been initialized via
     *         {@link #initialize(IServiceContainer)} or {@link #replaceContainer(IServiceContainer)},
     *         {@code false} otherwise
     *
     * @see #initialize(IServiceContainer)
     * @see #replaceContainer(IServiceContainer)
     * @see #reset()
     */
    public static boolean isInitialized() {
        return initialized;
    }

    // ########################################################
    // ### Service Registration Methods (IServiceRegistrar) ###
    // ########################################################

    /**
     * Registers a service instance under a given type.
     *
     * <p><b>Important:</b> This method delegates to the underlying service container's
     * registration logic. The exact behavior (overwrite policy, duplicate handling) depends
     * on the container implementation. Typically, attempting to register a duplicate service
     * will throw an exception.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     // Initialize first
     *     Services.initialize(new SimpleServiceContainer());
     *
     *     // Register a service instance
     *     IBoardGenerator generator = new DefaultBoardGenerator();
     *     Services.register(IBoardGenerator.class, generator);
     * }</pre>
     *
     * <p><b>Invalid Example (before initialization):</b></p>
     * <pre>{@code
     *     // Attempt to register before initialization
     *     Services.register(IBoardGenerator.class, new DefaultBoardGenerator());
     *     // throws IllegalStateException: Services facade has not been initialized
     * }</pre>
     *
     * <p><b>Invalid Example (null parameters):</b></p>
     * <pre>{@code
     *     // Attempt to register with null type
     *     Services.register(null, new DefaultBoardGenerator());
     *     // throws IllegalArgumentException: type is marked non-null but is null
     *
     *     // Attempt to register null instance
     *     Services.register(IBoardGenerator.class, null);
     *     // throws IllegalArgumentException: instance is marked non-null but is null
     * }</pre>
     *
     * @param type the class type to register the service under, must not be null
     * @param instance the service instance to register, must not be null
     * @param <T> the type of the service
     * @throws IllegalStateException if the Services facade has not been initialized
     * @throws IllegalArgumentException if either type or instance is null
     * @throws RuntimeException if the underlying container rejects the registration
     *         (e.g., duplicate service, container-specific constraints)
     *
     * @see #register(Class, Supplier)
     * @see #registerIfAbsent(Class, Object)
     * @see #replace(Class, Object)
     */
    public static <T> void register(@NotNull Class<T> type, @NotNull T instance) {
        ensureInitialized();
        serviceContainer.register(type, instance);
    }

    /**
     * Registers a service instance only if no service of the given type is currently registered.
     *
     * <p><b>Important:</b> This method will not overwrite an existing service registration.
     * If a service of the specified type already exists, this call will be silently ignored.</p>
     *
     * <p><b>Valid Example (first registration succeeds):</b></p>
     * <pre>{@code
     *     IBoardGenerator generator = new DefaultBoardGenerator();
     *     Services.registerIfAbsent(IBoardGenerator.class, generator);
     *     // Registration succeeds - no service existed previously
     * }</pre>
     *
     * <p><b>Valid Example (subsequent registration ignored):</b></p>
     * <pre>{@code
     *     Services.registerIfAbsent(IBoardGenerator.class, new CustomBoardGenerator());
     *     // Registration ignored - service already exists
     * }</pre>
     *
     * <p><b>Comparison with {@link #register(Class, Object)}:</b></p>
     * <ul>
     *     <li>{@code register()} throws an exception if service already exists</li>
     *     <li>{@code registerIfAbsent()} silently ignores if service already exists</li>
     * </ul>
     *
     * @param <T> the type of the service
     * @param type the class type to register the service under, must not be null
     * @param instance the service instance to register, must not be null
     * @throws IllegalStateException if the Services facade has not been initialized
     * @throws IllegalArgumentException if type or instance is null
     *
     * @see #register(Class, Object)
     * @see #registerIfAbsent(Class, Supplier)
     * @see #replace(Class, Object)
     */
    public static <T> void registerIfAbsent(@NotNull Class<T> type, @NotNull T instance) {
        ensureInitialized();
        serviceContainer.registerIfAbsent(type, instance);
    }

    /**
     * Registers a service supplier under a given type.
     *
     * <p><b>Important:</b> A service can only be registered once. Attempting to
     * register another supplier for the same type will throw an exception.</p>
     *
     * <p>The supplier will be invoked lazily when the service is first requested,
     * allowing for deferred initialization of expensive services.</p>
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     Services.register(ILogger.class, () -> new FileLogger("app.log"));
     *
     *     Services.register(IDatabaseService.class, () -> {
     *         DatabaseConfig config = loadDatabaseConfig();
     *         return new DatabaseService(config);
     *     });
     * }</pre>
     *
     * <p><b>Invalid Example (duplicate registration):</b></p>
     * <pre>{@code
     *     Services.register(ILogger.class, () -> new ConsoleLogger());
     *     Services.register(ILogger.class, () -> new FileLogger("app.log"));
     *     // throws exception: Service already registered for type ILogger
     * }</pre>
     *
     * <p><b>Invalid Example (null parameters):</b></p>
     * <pre>{@code
     *     Services.register(null, () -> new DefaultService());
     *     // throws IllegalArgumentException: type cannot be null
     *
     *     Services.register(IService.class, null);
     *     // throws IllegalArgumentException: supplier cannot be null
     * }</pre>
     *
     * @param type the class type to register the service supplier under
     * @param supplier the supplier that provides the service instance when needed
     * @param <T> the type of the service
     * @throws IllegalArgumentException if type or supplier is null
     * @throws IllegalStateException if the Services facade has not been initialized,
     *         or if a service is already registered for the given type
     * @throws RuntimeException if the underlying service container throws an exception
     *         during registration
     *
     * @see #register(Class, Object)
     * @see #registerIfAbsent(Class, Supplier)
     * @see #replace(Class, Supplier)
     */
    public static <T> void register(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier) {
        ensureInitialized();
        serviceContainer.register(type, supplier);
    }

    /**
     * Registers a service supplier only if no service of the given type is currently registered.
     *
     * <p><b>Important:</b> This method will not overwrite an existing service registration.
     * If a service of the specified type already exists, the supplier will not be invoked
     * and the existing service will remain unchanged.</p>
     *
     * <p><b>Valid Example (first registration):</b></p>
     * <pre>{@code
     *     Services.registerIfAbsent(ILogger.class, () -> new FileLogger("app.log"));
     *     // Registers the supplier since no ILogger service exists
     * }</pre>
     *
     * <p><b>Valid Example (skip registration):</b></p>
     * <pre>{@code
     *     // First register a service
     *     Services.register(ILogger.class, new ConsoleLogger());
     *
     *     // Attempt to register another - this will be skipped
     *     Services.registerIfAbsent(ILogger.class, () -> new FileLogger("app.log"));
     *     // ConsoleLogger remains registered, supplier is never invoked
     * }</pre>
     *
     * <p><b>Invalid Example (null type):</b></p>
     * <pre>{@code
     *     Services.registerIfAbsent(null, () -> new FileLogger("app.log"));
     *     // throws IllegalArgumentException: type is marked non-null but is null
     * }</pre>
     *
     * <p><b>Invalid Example (null supplier):</b></p>
     * <pre>{@code
     *     Services.registerIfAbsent(ILogger.class, null);
     *     // throws IllegalArgumentException: supplier is marked non-null but is null
     * }</pre>
     *
     * @param <T> the type of the service to register
     * @param type the class type to register the service under, must not be null
     * @param supplier the supplier that provides the service instance, must not be null
     * @throws IllegalStateException if the Services facade has not been initialized
     * @throws IllegalArgumentException if either type or supplier is null
     *
     * @see #register(Class, Supplier)
     * @see #registerIfAbsent(Class, Object)
     * @see #replace(Class, Supplier)
     */
    public static <T> void registerIfAbsent(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier) {
        ensureInitialized();
        serviceContainer.registerIfAbsent(type, supplier);
    }

    /**
     * Replaces an existing service registration with a new instance.
     * <p>
     * This operation will fail if no service of the specified type is currently registered.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     // Making sure a ICoolService is registered
     *     Services.registerServiceIfAbsent(ICoolService.class, new CoolService());
     *
     *     // A ICoolService Service has been registered before this call
     *     Services.replaceService(ICoolService.class, new NewCoolerService());
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     Services.replaceService(null, new CoolService());
     *
     *     // Null instance
     *     Services.replaceService(ICoolService.class, null);
     *
     *     // A ICoolService Service has NOT been registered before this call
     *     Services.replaceService(ICoolService.class, new CoolService());
     * }</pre>
     *
     * @param <T> the type of the service to replace
     * @param type the class object representing the service type
     * @param instance the new service instance
     * @throws ServiceNotFoundException if no service of the given type is registered
     * @throws ServiceProtectedException if trying to replace a critical service
     * @throws IllegalArgumentException if either parameter is null
     *
     * @see #register(Class, Object)
     * @see #registerIfAbsent(Class, Object)
     * @see #replace(Class, Supplier)
     */
    public static <T> void replace(@NotNull Class<T> type, @NotNull T instance) {
        ensureInitialized();
        serviceContainer.replace(type, instance);
    }

    /**
     * Replaces an existing service registration with a new supplier.
     * <p>
     * The supplier will be invoked when the service is next requested. This operation
     * will fail if no service of the specified type is currently registered.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     // Making sure a ICoolService is registered
     *     Services.registerServiceIfAbsent(ICoolService.class, () -> new CoolService());
     *
     *     // A ICoolService Service has been registered before this call
     *     Services.replaceService(ICoolService.class, () -> new NewCoolerService());
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     Services.replaceService(null, () -> new CoolService());
     *
     *     // Null Supplier
     *     Services.replaceService(ICoolService.class, null);
     *
     *     // Supplier that returns null, Will throw exception when requested
     *     Services.replaceService(ICoolService.class, () -> null);
     *
     *     // A ICoolService Service has NOT been registered before this call
     *     Services.replaceService(ICoolService.class, () -> new CoolService());
     * }</pre>
     *
     * @param <T> the type of the service to replace
     * @param type the class object representing the service type
     * @param supplier the new supplier that will provide the service instance
     * @throws ServiceNotFoundException if no service of the given type is registered
     * @throws ServiceProtectedException if trying to replace a critical service
     * @throws IllegalArgumentException if either parameter is null
     *
     * @see #register(Class, Supplier)
     * @see #registerIfAbsent(Class, Supplier)
     * @see #replace(Class, Object)
     */
    public static <T> void replace(@NotNull Class<T> type, @NotNull Supplier<? extends T> supplier) {
        ensureInitialized();
        serviceContainer.replace(type, supplier);
    }

    /**
     * Removes the service registration for the specified type.
     * <p>
     * If no service of the given type is registered, this method returns silently.
     *
     * <p><b>Valid Example:</b></p>
     * <pre>{@code
     *     // Making sure a ICoolService is registered
     *     Services.registerServiceIfAbsent(ICoolService.class, () -> new CoolService());
     *
     *     // A ICoolService Service has been registered before this call
     *     Services.unregisterService(ICoolService.class);
     * }</pre>
     *
     * <p><b>Invalid Examples:</b></p>
     * <pre>{@code
     *     // Null class
     *     Services.unregisterService(null);
     * }</pre>
     *
     * @param <T> the type of the service to unregister
     * @param type the class object representing the service type
     * @throws IllegalArgumentException if the type parameter is null
     *
     * @see #replace(Class, Object)
     * @see #replace(Class, Supplier)
     */
    public static <T> void unregister(@NotNull Class<T> type) {
        ensureInitialized();
        serviceContainer.unregister(type);
    }

    // --- Service Location Methods (IServiceLocator) ---

    /**
     * Checks whether the Services facade contains a registration for the specified type.
     *
     * <p><b>Note:</b> This method should return {@code false} if the type is not registered,
     * rather than throwing an exception. This makes it suitable for pre-checking before
     * calling {@code requireService()}.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     *     if (Services.contains(IBoardGenerator.class)) {
     *         // Safe to call requireService() without getting a exception
     *         IBoardGenerator generator = Services.requireService(IBoardGenerator.class);
     *     }
     * }</pre>
     *
     * @param type the class type to check for registration
     * @param <T> the type of the service
     * @return {@code true} if the service is registered, {@code false} otherwise
     *
     * @see #require(Class)
     * @see #get(Class)
     */
    public static <T> boolean contains(@NotNull Class<T> type) {
        ensureInitialized();
        return serviceContainer.contains(type);
    }

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
     *     Optional<IBoardGenerator> generator = Services.get(IBoardGenerator.class);
     *     generator.ifPresent(g -> g.generateBoard());
     * }</pre>
     *
     * @param type the class type of the service to retrieve
     * @param <T> the type of the service
     * @return an {@code Optional} containing the service instance if found, otherwise empty
     *
     * @see #contains(Class)
     * @see #require(Class)
     */
    public static <T> Optional<T> get(@NotNull Class<T> type) {
        ensureInitialized();
        return serviceContainer.get(type);
    }

    /**
     * Retrieves a service instance of the specified type, throwing an unchecked exception if not found.
     *
     * <p><b>Important:</b> This method should only be used when the caller is certain the service
     * exists, or when the absence of the service represents a fatal error condition.</p>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     *     try {
     *         IBoardGenerator generator = Services.require(IBoardGenerator.class);
     *         generator.generateBoard();
     *     } catch (ServiceNotFoundException e) {
     *         // Handle missing required service
     *     }
     * }</pre>
     *
     * @param type the class type of the service to retrieve
     * @param <T> the type of the service
     * @return the service instance (never null)
     * @throws ServiceNotFoundException if the service is not registered
     * @throws ServiceInstantiationException if a lazy supplier returned a null value
     *
     * @see #contains(Class)
     * @see #get(Class)
     */
    public static <T> T require(@NotNull Class<T> type) {
        ensureInitialized();
        return serviceContainer.require(type);
    }

    // --- Utility Methods ---

    /**
     * Returns direct access to the underlying service locator interface.
     *
     * <p><b>Important:</b> This method should be used sparingly. Prefer using the static
     * service location methods ({@link #get(Class)}, {@link #require(Class)},
     * {@link #contains(Class)}) instead of obtaining the locator interface directly.</p>
     *
     * <p><b>Primary Use Cases:</b></p>
     * <ul>
     *     <li>Framework integration requiring the raw locator interface</li>
     *     <li>Advanced scenarios where the static methods are insufficient</li>
     *     <li>Testing and mocking setups</li>
     * </ul>
     *
     * <p><b>Valid Example (framework integration):</b></p>
     * <pre>{@code
     *     // In framework setup code only
     *     IServiceLocator locator = Services.getLocator();
     *     frameworkComponent.setServiceLocator(locator);
     * }</pre>
     *
     * <p><b>Invalid Example (regular application code):</b></p>
     * <pre>{@code
     *     // Avoid this - use static methods instead
     *     IServiceLocator locator = Services.getLocator();
     *     Optional<ILogger> logger = locator.get(ILogger.class);
     *     // Prefer: Optional<ILogger> logger = Services.get(ILogger.class);
     * }</pre>
     *
     * @return the underlying service locator interface
     * @throws IllegalStateException if the Services facade has not been initialized
     *
     * @see #get(Class)
     * @see #require(Class)
     * @see #contains(Class)
     */
    public static IServiceLocator getLocator() {
        ensureInitialized();
        return serviceContainer;
    }

    /**
     * Returns direct access to the underlying service registrar interface.
     *
     * <p><b>Important:</b> This method should be used sparingly and primarily during
     * the initialization phase. Prefer using the static registration methods
     * ({@link #register(Class, Object)}, {@link #registerIfAbsent(Class, Object)},
     * etc.) for most use cases.</p>
     *
     * <p><b>Typical Use Case (initialization phase):</b></p>
     * <pre>{@code
     *     // During application startup configuration
     *     IServiceRegistrar registrar = Services.getRegistrar();
     *     registrar.registerIfAbsent(IConfigService.class, new ConfigService());
     *     registrar.registerIfAbsent(ILogger.class, new FileLogger("app.log"));
     * }</pre>
     *
     * <p><b>Discouraged Use Case (runtime usage):</b></p>
     * <pre>{@code
     *     // Avoid using getRegistrar() during normal operation
     *     public void someRuntimeMethod() {
     *         IServiceRegistrar registrar = Services.getRegistrar();
     *         registrar.registerIfAbsent(ITemporaryService.class, new TemporaryService());
     *         // This breaks the encapsulation pattern intended by the facade
     *     }
     * }</pre>
     *
     * <p><b>Invalid Usage Examples:</b></p>
     * <pre>{@code
     *     // Attempt to use before initialization
     *     IServiceRegistrar registrar = Services.getRegistrar();
     *     // throws IllegalStateException: Services facade has not been initialized
     * }</pre>
     *
     * @return the underlying service registrar interface
     * @throws IllegalStateException if the Services facade has not been initialized
     *
     * @see #getLocator()
     * @see #register(Class, Object)
     * @see #registerIfAbsent(Class, Object)
     * @see #replace(Class, Object)
     * @see #unregister(Class)
     */
    public static IServiceRegistrar getRegistrar() {
        ensureInitialized();
        return serviceContainer;
    }

    /**
     * Ensures that the Services facade has been properly initialized before operation.
     *
     * <p>This method performs a validation check to confirm that both the initialization flag
     * is set and the service container reference is non-null. This double-check prevents
     * potential race conditions during initialization.</p>
     *
     * <p><b>Typical Usage:</b> This method is called internally by all public static methods
     * before delegating to the service container to ensure proper initialization state.</p>
     *
     * <p><b>Exception Example:</b></p>
     * <pre>{@code
     *     // Without initialization
     *     Services.getService(IBoardGenerator.class);
     *     // internally calls ensureInitialized() which throws:
     *     // IllegalStateException: Services facade has not been initialized
     * }</pre>
     *
     * @throws IllegalStateException if the Services facade has not been initialized
     *         (either initialized flag is false or serviceContainer is null)
     */
    private static void ensureInitialized() {
        if (!initialized || serviceContainer == null) {
            throw new IllegalStateException("Services facade has not been initialized. Call Services.initialize() first.");
        }
    }
}
