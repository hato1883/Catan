package io.github.hato1883.api.events.registry.structure.port;

import io.github.hato1883.api.events.registry.structure.StructureTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IPortType;
import io.github.hato1883.api.registries.IRegistry;

public class PortTypeUnregisterEvent extends StructureTypeUnregisterEvent<IPortType> {
    public PortTypeUnregisterEvent(IRegistry<IPortType> registry, String id, IPortType entry) {
        super(registry, id, entry);
    }
}
