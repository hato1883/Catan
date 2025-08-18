package io.github.hato1883.core.game.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.ITileType;

import java.util.Map;

// catan-base only
public class TileTypeImpl implements ITileType {
    private final Identifier id;
    private final Map<IResourceType, Integer> baseProduction;

    TileTypeImpl(Identifier id, Map<IResourceType, Integer> baseProduction) {
        this.id = id;
        this.baseProduction = baseProduction;
    }


    @Override
    public Identifier getId() { return id; }

    @Override
    public Map<IResourceType, Integer> getBaseProduction() { return baseProduction; }

    @Override
    public String toString() {
        return "TileType{" +
            "'" + id + '\'' +
            " -> " + baseProduction +
            '}';
    }
}

