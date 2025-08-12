package io.github.hato1883.api.events.registry.structure;

import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.game.board.IStructureType;
import io.github.hato1883.api.registries.IRegistry;

public class StructureTypeReplaceEvent<T extends IStructureType> extends RegistryReplaceEvent<T> {
    public StructureTypeReplaceEvent(IRegistry<T> registry, String id, T oldEntry, T newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
