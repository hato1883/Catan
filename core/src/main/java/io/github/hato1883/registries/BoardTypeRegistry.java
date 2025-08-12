package io.github.hato1883.registries;

import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.board.BoardTypeRegisterEvent;
import io.github.hato1883.api.events.registry.board.BoardTypeReplaceEvent;
import io.github.hato1883.api.events.registry.board.BoardTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IBoardType;

public class BoardTypeRegistry extends Registry<IBoardType> {

    @Override
    protected RegistryRegisterEvent<IBoardType> createRegistryRegisterEvent(String id, IBoardType element) {
        return new BoardTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IBoardType> createRegistryReplaceEvent(String id, IBoardType oldElement, IBoardType newElement) {
        return new BoardTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IBoardType> createRegistryUnregisterEvent(String id, IBoardType element) {
        return new BoardTypeUnregisterEvent(this, id, element);
    }
}
