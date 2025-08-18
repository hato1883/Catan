package io.github.hato1883.api.events.registry.structure;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.game.board.IStructureType;
import io.github.hato1883.api.registries.IRegistry;

public class StructureTypeUnregisterEvent<T extends IStructureType> extends RegistryUnregisterEvent<T> {
    public StructureTypeUnregisterEvent(IRegistry<T> registry, Identifier id, T entry) {
        super(registry, id, entry);
    }
}
