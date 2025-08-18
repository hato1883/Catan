package io.github.hato1883.basemod.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.game.board.*;
import io.github.hato1883.core.game.board.SpiralUtils;

import java.util.*;

import static io.github.hato1883.basemod.Main.MOD_ID;
import static io.github.hato1883.basemod.board.ClassicHexIslandBoard.DEFAULT_NUMBERS;
import static io.github.hato1883.basemod.board.ClassicHexIslandBoard.DEFAULT_TILE_IDS;

public class DonutBoardType implements IBoardType {

    @Override public Identifier getIdentifier() { return Identifier.of(MOD_ID, "donut"); }
    @Override public String getName() { return "Donut Board"; }
    @Override public IShapeGenerator getShapeGenerator() { return new DonutShapeGenerator(); }
    @Override public BoardGenerationConfig getDefaultConfig() { return new BoardGenerationConfig(4, true); }

    private final List<Identifier> tilesLeftToPlace = new ArrayList<>(DEFAULT_TILE_IDS);
    private final List<Integer> tokensLeftToPlace = new ArrayList<>(DEFAULT_NUMBERS);

    @Override
    public List<ICubeCoord> getCubeOrder(Set<ICubeCoord> coords, BoardGenerationConfig config, Random rng) {
        return SpiralUtils.spiralOrder(coords, config.shouldShuffleTiles() ? rng : null); // default ordering
    }

    @Override
    public Optional<ITileType> chooseTile(ICubeCoord coord, BoardGenerationConfig config, Random rng) {
        if (tilesLeftToPlace.isEmpty())
            tilesLeftToPlace.addAll(DEFAULT_TILE_IDS);
        Identifier tileId = tilesLeftToPlace.removeFirst();
        return Optional.of(Registries.tiles().require(tileId));
    }

    @Override
    public Optional<Collection<Integer>> assignNumbers(ICubeCoord coord, BoardGenerationConfig config, Random rng) {
        if (tokensLeftToPlace.isEmpty())
            tokensLeftToPlace.addAll(DEFAULT_NUMBERS);
        Collection<Integer> tokens = new ArrayList<Integer>();
        tokens.add(tokensLeftToPlace.removeFirst());
        return Optional.of(tokens);
    }
}
