package io.github.hato1883.api.events.registry.structure.road;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.structure.StructureTypeRegisterEvent;
import io.github.hato1883.api.world.board.IRoadType;
import io.github.hato1883.api.registries.IRegistry;

public class RoadTypeRegisterEvent extends StructureTypeRegisterEvent<IRoadType> {
    public RoadTypeRegisterEvent(IRegistry<IRoadType> registry, Identifier id, IRoadType entry) {
        super(registry, id, entry);
    }
}
