package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.structure.building.BuildingTypeRegisterEvent;
import io.github.hato1883.api.events.registry.structure.building.BuildingTypeReplaceEvent;
import io.github.hato1883.api.events.registry.structure.building.BuildingTypeUnregisterEvent;
import io.github.hato1883.api.world.board.IBuildingType;
import io.github.hato1883.api.registries.IBuildingTypeRegistry;

public class BuildingTypeRegistry extends Registry<IBuildingType> implements IBuildingTypeRegistry {

    /* Example method */
    public IBuildingType getTown() {
        return get(Identifier.of("basemod", "town")).orElseThrow(() ->
            new IllegalStateException("Town building is not registered"));
    }

    /* Example method */
    public IBuildingType getCity() {
        return get(Identifier.of("basemod", "city")).orElseThrow(() ->
            new IllegalStateException("City building is  not registered"));
    }

    @Override
    protected RegistryRegisterEvent<IBuildingType> createRegistryRegisterEvent(Identifier id, IBuildingType element) {
        return new BuildingTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IBuildingType> createRegistryReplaceEvent(Identifier id, IBuildingType oldElement, IBuildingType newElement) {
        return new BuildingTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IBuildingType> createRegistryUnregisterEvent(Identifier id, IBuildingType element) {
        return new BuildingTypeUnregisterEvent(this, id, element);
    }
}
