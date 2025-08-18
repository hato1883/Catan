package io.github.hato1883.core.game.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.factories.ITileTypeFactory;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;

import java.util.Map;

// catan-base only
public class TileTypeFactoryImpl implements ITileTypeFactory {
    public ITileType create(Identifier id, Map<IResourceType, Integer> baseProduction) {
        return new TileTypeImpl(id, baseProduction);
    }
}

