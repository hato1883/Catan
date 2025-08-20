package io.github.hato1883.api.events.registry.tile;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.world.board.ITileType;
import io.github.hato1883.api.registries.IRegistry;

public class TileTypeUnregisterEvent extends RegistryUnregisterEvent<ITileType> {
    public TileTypeUnregisterEvent(IRegistry<ITileType> registry, Identifier id, ITileType entry) {
        super(registry, id, entry);
    }
}
