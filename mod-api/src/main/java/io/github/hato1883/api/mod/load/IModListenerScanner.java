package io.github.hato1883.api.mod.load;

import io.github.hato1883.api.events.IEventListenerRegistrar;

import java.util.List;

/**
 * Scans supplied loaded mods for listener classes and registers them using
 * an external ListenerRegistrar. Implementations may use parallel scanning;
 * executor is injected in concrete classes.
 */
public interface IModListenerScanner {
    void scanAndRegister(List<ILoadedMod> mods);
}
