package io.github.hato1883.api.events.registry.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.game.board.IBoardType;
import io.github.hato1883.api.registries.IRegistry;

public class BoardTypeRegisterEvent extends RegistryRegisterEvent<IBoardType> {
    public BoardTypeRegisterEvent(IRegistry<IBoardType> registry, Identifier id, IBoardType entry) {
        super(registry, id, entry);
    }
}
