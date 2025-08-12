package io.github.hato1883.api.modding;

/**
 * Base interface for all Settlers of Catan mods.
 * <p>
 * Every mod must have exactly one class implementing this interface
 * as its entrypoint. The mod loader will automatically discover and
 * invoke the lifecycle methods defined here based on the game's state.
 * </p>
 * <p>
 * The lifecycle is as follows:
 * <ol>
 *   <li>{@link #registerModContent(IModRegistrar)} — Called immediately when the mod is loaded into memory.</li>
 *   <li>{@link #onInitialize()} — Called after all mods have registered their
 *                                 content in {@link #registerModContent} phase </li>
 *   <li>{@link #onLoad()} — Called after all core game resources, assets, and systems are ready.</li>
 *   <li>{@link #onEnable()} — Called when the mod is explicitly enabled in-game or via config.</li>
 *   <li>{@link #onDisable()} — Called when the mod is explicitly disabled in-game or via config.</li>
 * </ol>
 * Only {@code onInitialize()} is required to be implemented; all other methods have default no-op implementations.
 * </p>
 */
public interface CatanMod {

    /**
     * Called as soon as the mod is loaded into memory by the mod loader.
     * <p>
     * This method is invoked before most of the game's systems have been initialized.
     * It should be used for registering custom Tiles/Resources/Structures/etc.
     * </p>
     * <p>
     * Avoid interacting with world data or game state here, as those systems may not
     * be initialized yet.
     * </p>
     * <p>
     * This method is optional and has a default empty implementation.
     * </p>
     */
    default void registerModContent(IModRegistrar registrar) {}

    /**
     * Called as soon as the mod is loaded into memory by the mod loader but after {@link #registerModContent}.
     * <p>
     * This method is invoked before most of the game's systems have been initialized.
     * It should be used for setup unrelated to registration of content.
     * </p>
     * <p>
     * Avoid interacting with world data or game state here, as those systems may not
     * be initialized yet.
     * </p>
     */
    void onInitialize();

    /**
     * Called once the game's core resources, assets, and registries are ready.
     * <p>
     * This is the recommended place to perform setup that depends on game
     * resources (textures, boards, registries, etc.).
     * </p>
     * <p>
     * This method is optional and has a default empty implementation.
     * </p>
     */
    default void onLoad() {}

    /**
     * Called when the mod is explicitly enabled.
     * <p>
     * This may be triggered automatically during game start if the mod is
     * enabled by default, or manually through a configuration file or UI.
     * </p>
     * <p>
     * Use this hook to register runtime logic, enable features, or resume
     * paused systems.
     * </p>
     * <p>
     * This method is optional and has a default empty implementation.
     * </p>
     */
    default void onEnable() {}

    /**
     * Called when the mod is explicitly disabled.
     * <p>
     * Use this to unregister listeners, stop running tasks, or clean up
     * runtime resources. This may occur without the game fully shutting down
     * (e.g., disabling mods in a running server).
     * </p>
     * <p>
     * This method is optional and has a default empty implementation.
     * </p>
     */
    default void onDisable() {}
}

