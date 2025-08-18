package io.github.hato1883.api.events.registry.structure.building;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.structure.StructureTypeRegisterEvent;
import io.github.hato1883.api.game.board.IBuildingType;
import io.github.hato1883.api.registries.IRegistry;

public class BuildingTypeRegisterEvent extends StructureTypeRegisterEvent<IBuildingType> {
    public BuildingTypeRegisterEvent(IRegistry<IBuildingType> registry, Identifier id, IBuildingType entry) {
        super(registry, id, entry);
    }
}
