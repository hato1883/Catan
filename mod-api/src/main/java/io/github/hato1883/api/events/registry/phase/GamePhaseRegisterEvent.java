package io.github.hato1883.api.events.registry.phase;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.registries.IRegistry;

public class GamePhaseRegisterEvent extends RegistryRegisterEvent<IGamePhase> {
    public GamePhaseRegisterEvent(IRegistry<IGamePhase> registry, String id, IGamePhase entry) {
        super(registry, id, entry);
    }
}
