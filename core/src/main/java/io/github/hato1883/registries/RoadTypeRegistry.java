package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.structure.road.RoadTypeRegisterEvent;
import io.github.hato1883.api.events.registry.structure.road.RoadTypeReplaceEvent;
import io.github.hato1883.api.events.registry.structure.road.RoadTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IRoadType;

public class RoadTypeRegistry extends Registry<IRoadType> {

    /* Example method */
    public IRoadType getDefaultRoad() {
        return get("catan:road").orElseThrow(() ->
            new IllegalStateException("Default road is not registered"));
    }

    @Override
    protected RegistryRegisterEvent<IRoadType> createRegistryRegisterEvent(String id, IRoadType element) {
        return new RoadTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IRoadType> createRegistryReplaceEvent(String id, IRoadType oldElement, IRoadType newElement) {
        return new RoadTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IRoadType> createRegistryUnregisterEvent(String id, IRoadType element) {
        return new RoadTypeUnregisterEvent(this, id, element);
    }
}
