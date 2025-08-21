package io.github.hato1883.api.events.registry;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.registries.IRegistry;

/**
 * Fired after an entry has been registered in a registry.
 */
public class RegistryPostRegisterEvent<T> extends RegistryEvent<T> {
    public RegistryPostRegisterEvent(IRegistry<T> registry, Identifier id, T entry) {
        super(registry, id, entry);
    }
}
