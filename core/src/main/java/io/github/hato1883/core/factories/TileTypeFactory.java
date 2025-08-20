package io.github.hato1883.core.factories;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.factories.ITileTypeFactory;
import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.ITileType;
import io.github.hato1883.core.game.world.board.TileType;

import java.util.Map;

// catan-base only
public class TileTypeFactory implements ITileTypeFactory {
    public ITileType create(Identifier id, Map<IResourceType, Integer> baseProduction) {
        return new TileType(id, baseProduction);
    }
}

