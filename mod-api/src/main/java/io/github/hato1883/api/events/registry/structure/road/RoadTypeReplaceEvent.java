package io.github.hato1883.api.events.registry.structure.road;

import io.github.hato1883.api.events.registry.structure.StructureTypeReplaceEvent;
import io.github.hato1883.api.game.board.IRoadType;
import io.github.hato1883.api.registries.IRegistry;

public class RoadTypeReplaceEvent extends StructureTypeReplaceEvent<IRoadType> {
    public RoadTypeReplaceEvent(IRegistry<IRoadType> registry, String id, IRoadType oldEntry, IRoadType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
