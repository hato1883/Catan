package io.github.hato1883.api.events.registry.tile;

import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.game.board.ITileType;
import io.github.hato1883.api.registries.IRegistry;

public class TileTypeReplaceEvent extends RegistryReplaceEvent<ITileType> {
    public TileTypeReplaceEvent(IRegistry<ITileType> registry, String id, ITileType oldEntry, ITileType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
