package io.github.hato1883.api.game;

public interface IResourceBank {
    int getAmount(IResourceType type);
    void add(IResourceType type, int amount);
    boolean remove(IResourceType type, int amount);
}
