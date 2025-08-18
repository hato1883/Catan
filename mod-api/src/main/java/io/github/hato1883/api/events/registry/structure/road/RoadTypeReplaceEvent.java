package io.github.hato1883.api.events.registry.structure.road;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.structure.StructureTypeReplaceEvent;
import io.github.hato1883.api.game.board.IRoadType;
import io.github.hato1883.api.registries.IRegistry;

public class RoadTypeReplaceEvent extends StructureTypeReplaceEvent<IRoadType> {
    public RoadTypeReplaceEvent(IRegistry<IRoadType> registry, Identifier id, IRoadType oldEntry, IRoadType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
