package io.github.hato1883.api.events.modding;

import io.github.hato1883.api.events.GameEvent;
import io.github.hato1883.api.game.IGameState;
import io.github.hato1883.api.modding.CatanMod;

/**
 * Base class for all mod lifecycle-related events.
 * <p>
 * This event is fired whenever a mod is loaded, unloaded, enabled, or disabled.
 * All mod-related event classes extend this base to provide shared context.
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * eventBus.registerListener(ModLoadEvent.class, event -> {
 *     CatanMod mod = event.getMod();
 *     System.out.println("Mod loaded: " + mod.getName());
 * });
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *   <li>{@link ModLoadEvent}</li>
 *   <li>{@link ModUnloadEvent}</li>
 *   <li>{@link ModEnableEvent}</li>
 *   <li>{@link ModDisableEvent}</li>
 * </ul>
 */
public abstract class ModEvent extends GameEvent {

    private final CatanMod mod;

    /**
     * Constructs a new ModEvent.
     *
     * @param gameState the current game state
     * @param mod       the mod associated with this event
     */
    public ModEvent(IGameState gameState, CatanMod mod) {
        super(gameState);
        this.mod = mod;
    }

    /**
     * Gets the mod associated with this event.
     *
     * @return the mod instance
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * CatanMod mod = event.getMod();
     * System.out.println("Affected mod: " + mod.getName());
     * }</pre>
     *
     * <h3>See Also:</h3>
     * <ul>
     *   <li>{@link CatanMod}</li>
     * </ul>
     */
    public CatanMod getMod() {
        return mod;
    }
}

