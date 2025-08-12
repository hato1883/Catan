package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.phase.GamePhaseRegisterEvent;
import io.github.hato1883.api.events.registry.phase.GamePhaseReplaceEvent;
import io.github.hato1883.api.events.registry.phase.GamePhaseUnregisterEvent;
import io.github.hato1883.api.game.IGamePhase;

public class GamePhaseRegistry extends Registry<IGamePhase> {

    @Override
    protected RegistryRegisterEvent<IGamePhase> createRegistryRegisterEvent(String id, IGamePhase element) {
        return new GamePhaseRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IGamePhase> createRegistryReplaceEvent(String id, IGamePhase oldElement, IGamePhase newElement) {
        return new GamePhaseReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IGamePhase> createRegistryUnregisterEvent(String id, IGamePhase element) {
        return new GamePhaseUnregisterEvent(this, id, element);
    }
}
