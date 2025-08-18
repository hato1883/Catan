package io.github.hato1883.core.game.resource;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.factories.IResourceTypeFactory;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;
import io.github.hato1883.core.game.board.TileTypeImpl;

import java.util.Map;

public class ResourceTypeFactoryImpl implements IResourceTypeFactory {

    @Override
    public IResourceType create(Identifier id, String name, String description) {
        return new ResourceTypeImpl(id, name, description);
    }

    @Override
    public IResourceType create(Identifier id, String name) {
        return new ResourceTypeImpl(id, name);
    }
}
