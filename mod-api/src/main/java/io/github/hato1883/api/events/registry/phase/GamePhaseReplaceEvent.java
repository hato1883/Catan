package io.github.hato1883.api.events.registry.phase;

import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.game.IGamePhase;
import io.github.hato1883.api.registries.IRegistry;

public class GamePhaseReplaceEvent extends RegistryReplaceEvent<IGamePhase> {
    public GamePhaseReplaceEvent(IRegistry<IGamePhase> registry, String id, IGamePhase oldEntry, IGamePhase newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
