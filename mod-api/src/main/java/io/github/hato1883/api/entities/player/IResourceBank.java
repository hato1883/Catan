package io.github.hato1883.api.entities.player;

import io.github.hato1883.api.entities.resource.IResourceType;

public interface IResourceBank {
    int getAmount(IResourceType type);
    void add(IResourceType type, int amount);
    boolean remove(IResourceType type, int amount);
}
