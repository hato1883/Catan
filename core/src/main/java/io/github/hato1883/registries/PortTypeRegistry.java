package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.structure.port.PortTypeRegisterEvent;
import io.github.hato1883.api.events.registry.structure.port.PortTypeReplaceEvent;
import io.github.hato1883.api.events.registry.structure.port.PortTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IPortType;

public class PortTypeRegistry extends Registry<IPortType> {

    @Override
    protected RegistryRegisterEvent<IPortType> createRegistryRegisterEvent(String id, IPortType element) {
        return new PortTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IPortType> createRegistryReplaceEvent(String id, IPortType oldElement, IPortType newElement) {
        return new PortTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IPortType> createRegistryUnregisterEvent(String id, IPortType element) {
        return new PortTypeUnregisterEvent(this, id, element);
    }
}
