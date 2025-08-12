package io.github.hato1883.api.events.registry.structure.building;

import io.github.hato1883.api.events.registry.structure.StructureTypeReplaceEvent;
import io.github.hato1883.api.game.board.IBuildingType;
import io.github.hato1883.api.registries.IRegistry;

public class BuildingTypeReplaceEvent extends StructureTypeReplaceEvent<IBuildingType> {
    public BuildingTypeReplaceEvent(IRegistry<IBuildingType> registry, String id, IBuildingType oldEntry, IBuildingType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
