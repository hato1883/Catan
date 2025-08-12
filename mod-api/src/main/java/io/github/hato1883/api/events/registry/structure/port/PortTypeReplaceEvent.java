package io.github.hato1883.api.events.registry.structure.port;

import io.github.hato1883.api.events.registry.structure.StructureTypeReplaceEvent;
import io.github.hato1883.api.game.board.IPortType;
import io.github.hato1883.api.registries.IRegistry;

public class PortTypeReplaceEvent extends StructureTypeReplaceEvent<IPortType> {
    public PortTypeReplaceEvent(IRegistry<IPortType> registry, String id, IPortType oldEntry, IPortType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
