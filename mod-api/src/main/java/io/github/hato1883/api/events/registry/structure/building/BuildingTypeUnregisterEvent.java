package io.github.hato1883.api.events.registry.structure.building;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.structure.StructureTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IBuildingType;
import io.github.hato1883.api.registries.IRegistry;

public class BuildingTypeUnregisterEvent extends StructureTypeUnregisterEvent<IBuildingType> {
    public BuildingTypeUnregisterEvent(IRegistry<IBuildingType> registry, Identifier id, IBuildingType entry) {
        super(registry, id, entry);
    }
}
