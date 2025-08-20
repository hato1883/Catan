package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.phase.GamePhaseRegisterEvent;
import io.github.hato1883.api.events.registry.phase.GamePhaseReplaceEvent;
import io.github.hato1883.api.events.registry.phase.GamePhaseUnregisterEvent;
import io.github.hato1883.api.world.phase.IGamePhase;
import io.github.hato1883.api.registries.IGamePhaseRegistry;

public class GamePhaseRegistry extends Registry<IGamePhase> implements IGamePhaseRegistry {

    @Override
    protected RegistryRegisterEvent<IGamePhase> createRegistryRegisterEvent(Identifier id, IGamePhase element) {
        return new GamePhaseRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IGamePhase> createRegistryReplaceEvent(Identifier id, IGamePhase oldElement, IGamePhase newElement) {
        return new GamePhaseReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IGamePhase> createRegistryUnregisterEvent(Identifier id, IGamePhase element) {
        return new GamePhaseUnregisterEvent(this, id, element);
    }
}
