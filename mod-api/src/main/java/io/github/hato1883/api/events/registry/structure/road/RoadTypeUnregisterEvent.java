package io.github.hato1883.api.events.registry.structure.road;

import io.github.hato1883.api.events.registry.structure.StructureTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IRoadType;
import io.github.hato1883.api.registries.IRegistry;

public class RoadTypeUnregisterEvent extends StructureTypeUnregisterEvent<IRoadType> {
    public RoadTypeUnregisterEvent(IRegistry<IRoadType> registry, String id, IRoadType entry) {
        super(registry, id, entry);
    }
}
