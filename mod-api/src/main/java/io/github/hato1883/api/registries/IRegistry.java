package io.github.hato1883.api.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.game.IResourceType;

import java.util.Collection;
import java.util.Optional;

public interface IRegistry<T> {
    void register(Identifier id, T element);
    void replace(Identifier id, T element);
    Optional<T> get(Identifier id);
    T require(Identifier id);
    Collection<T> getAll();
    boolean unregister(Identifier id);
    int unregisterAll(String modid);
    boolean isRegistered(Identifier id);
}
