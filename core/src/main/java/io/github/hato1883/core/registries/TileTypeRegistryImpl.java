package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.ModServices;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;
import io.github.hato1883.api.events.registry.tile.TileTypeRegisterEvent;
import io.github.hato1883.api.events.registry.tile.TileTypeReplaceEvent;
import io.github.hato1883.api.events.registry.tile.TileTypeUnregisterEvent;
import io.github.hato1883.api.factories.ITileTypeFactory;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;
import io.github.hato1883.api.registries.ITileTypeRegistry;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TileTypeRegistryImpl extends Registry<ITileType> implements ITileTypeRegistry {

    private ITileTypeFactory factory;

    public TileTypeRegistryImpl() {}

    /**
     * Allows core to inject a custom factory if needed.
     * This method is package-private and not exposed through the API.
     */
    void setFactory(ITileTypeFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        this.factory = factory;
    }

    @Override
    protected RegistryRegisterEvent<ITileType> createRegistryRegisterEvent(Identifier id, ITileType element) {
        return new TileTypeRegisterEvent(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<ITileType> createRegistryReplaceEvent(Identifier id, ITileType oldElement, ITileType newElement) {
        return new TileTypeReplaceEvent(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<ITileType> createRegistryUnregisterEvent(Identifier id, ITileType element) {
        return new TileTypeUnregisterEvent(this, id, element);
    }

    @Override
    public void register(@NotNull Identifier id, Map<IResourceType, Integer> baseProduction) {
        if (factory == null)
            factory = ModServices.requireService(ITileTypeFactory.class);
        ITileType tileType = factory.create(id, baseProduction);
        register(id, tileType);
    }

    /**
     * Returns a set of a tile types that can produce the given resource
     * @param type resource that tile must be able to produce
     * @return set of all tiles capable of producing the resource
     */
    public Set<ITileType> getProducerOf(IResourceType type) {
        Set<ITileType> producers = new HashSet<>();
        for (ITileType tile : getAll()) {
            if (tile.getBaseProduction().containsKey(type)) {
                producers.add(tile);
            }
        }
        return producers;
    }
}
