package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.structure.port.PortTypeRegisterEvent;
import io.github.hato1883.api.events.registry.structure.port.PortTypeReplaceEvent;
import io.github.hato1883.api.events.registry.structure.port.PortTypeUnregisterEvent;
import io.github.hato1883.api.world.board.IPortType;
import io.github.hato1883.api.registries.IPortTypeRegistry;

public class PortTypeRegistry extends Registry<IPortType> implements IPortTypeRegistry {

    public PortTypeRegistry(io.github.hato1883.api.events.IEventBusService eventBus) {
        super(eventBus);
    }

    @Override
    protected RegistryRegisterEvent<IPortType> createRegistryRegisterEvent(Identifier id, IPortType element) {
        return new PortTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IPortType> createRegistryReplaceEvent(Identifier id, IPortType oldElement, IPortType newElement) {
        return new PortTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IPortType> createRegistryUnregisterEvent(Identifier id, IPortType element) {
        return new PortTypeUnregisterEvent(this, id, element);
    }
}
