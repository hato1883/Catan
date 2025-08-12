package io.github.hato1883.api.events.registry.structure.road;

import io.github.hato1883.api.events.registry.structure.StructureTypeRegisterEvent;
import io.github.hato1883.api.game.board.IRoadType;
import io.github.hato1883.api.registries.IRegistry;

public class RoadTypeRegisterEvent extends StructureTypeRegisterEvent<IRoadType> {
    public RoadTypeRegisterEvent(IRegistry<IRoadType> registry, String id, IRoadType entry) {
        super(registry, id, entry);
    }
}
