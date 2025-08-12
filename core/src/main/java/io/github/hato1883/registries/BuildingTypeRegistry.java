package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.structure.building.BuildingTypeRegisterEvent;
import io.github.hato1883.api.events.registry.structure.building.BuildingTypeReplaceEvent;
import io.github.hato1883.api.events.registry.structure.building.BuildingTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IBuildingType;

public class BuildingTypeRegistry extends Registry<IBuildingType> {

    /* Example method */
    public IBuildingType getTown() {
        return get("catan:town").orElseThrow(() ->
            new IllegalStateException("Town building is not registered"));
    }

    /* Example method */
    public IBuildingType getCity() {
        return get("catan:city").orElseThrow(() ->
            new IllegalStateException("City building is  not registered"));
    }

    @Override
    protected RegistryRegisterEvent<IBuildingType> createRegistryRegisterEvent(String id, IBuildingType element) {
        return new BuildingTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IBuildingType> createRegistryReplaceEvent(String id, IBuildingType oldElement, IBuildingType newElement) {
        return new BuildingTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IBuildingType> createRegistryUnregisterEvent(String id, IBuildingType element) {
        return new BuildingTypeUnregisterEvent(this, id, element);
    }
}
