package io.github.hato1883.api.registries;

import java.util.Collection;
import java.util.Optional;

public interface IRegistry<T> {
    void register(String id, T element);
    void replace(String id, T element);
    Optional<T> get(String id);
    Collection<T> getAll();
    boolean unregister(String id);
    int unregisterAll(String modid);
}
