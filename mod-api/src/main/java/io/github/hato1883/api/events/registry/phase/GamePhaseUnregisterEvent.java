package io.github.hato1883.api.events.registry.phase;

import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.registries.IRegistry;

public class GamePhaseUnregisterEvent extends RegistryUnregisterEvent<IGamePhase> {
    public GamePhaseUnregisterEvent(IRegistry<IGamePhase> registry, String id, IGamePhase entry) {
        super(registry, id, entry);
    }
}
