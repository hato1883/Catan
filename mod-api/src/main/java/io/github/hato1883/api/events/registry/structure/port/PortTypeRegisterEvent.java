package io.github.hato1883.api.events.registry.structure.port;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.structure.StructureTypeRegisterEvent;
import io.github.hato1883.api.world.board.IPortType;
import io.github.hato1883.api.registries.IRegistry;

public class PortTypeRegisterEvent extends StructureTypeRegisterEvent<IPortType> {
    public PortTypeRegisterEvent(IRegistry<IPortType> registry, Identifier id, IPortType entry) {
        super(registry, id, entry);
    }
}
