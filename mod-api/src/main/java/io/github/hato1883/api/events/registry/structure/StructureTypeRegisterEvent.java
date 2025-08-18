package io.github.hato1883.api.events.registry.structure;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.game.board.IStructureType;
import io.github.hato1883.api.registries.IRegistry;

public class StructureTypeRegisterEvent<T extends IStructureType> extends RegistryRegisterEvent<T> {
    public StructureTypeRegisterEvent(IRegistry<T> registry, Identifier id, T entry) {
        super(registry, id, entry);
    }
}
