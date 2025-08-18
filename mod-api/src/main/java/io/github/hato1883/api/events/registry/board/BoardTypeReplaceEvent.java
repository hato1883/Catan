package io.github.hato1883.api.events.registry.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.game.board.IBoardType;
import io.github.hato1883.api.registries.IRegistry;

public class BoardTypeReplaceEvent extends RegistryReplaceEvent<IBoardType> {
    public BoardTypeReplaceEvent(IRegistry<IBoardType> registry, Identifier id, IBoardType oldEntry, IBoardType newEntry) {
        super(registry, id, oldEntry, newEntry);
    }
}
