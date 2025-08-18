package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.board.BoardTypeRegisterEvent;
import io.github.hato1883.api.events.registry.board.BoardTypeReplaceEvent;
import io.github.hato1883.api.events.registry.board.BoardTypeUnregisterEvent;
import io.github.hato1883.api.game.board.IBoardType;
import io.github.hato1883.api.registries.IBoardTypeRegistry;

public class BoardTypeRegistry extends Registry<IBoardType> implements IBoardTypeRegistry {

    @Override
    protected RegistryRegisterEvent<IBoardType> createRegistryRegisterEvent(Identifier id, IBoardType element) {
        return new BoardTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IBoardType> createRegistryReplaceEvent(Identifier id, IBoardType oldElement, IBoardType newElement) {
        return new BoardTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IBoardType> createRegistryUnregisterEvent(Identifier id, IBoardType element) {
        return new BoardTypeUnregisterEvent(this, id, element);
    }
}
