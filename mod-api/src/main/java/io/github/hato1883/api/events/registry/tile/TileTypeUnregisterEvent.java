package io.github.hato1883.api.events.registry.tile;

import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.game.board.ITileType;
import io.github.hato1883.api.registries.IRegistry;

public class TileTypeUnregisterEvent extends RegistryUnregisterEvent<ITileType> {
    public TileTypeUnregisterEvent(IRegistry<ITileType> registry, String id, ITileType entry) {
        super(registry, id, entry);
    }
}
