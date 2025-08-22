package io.github.hato1883.basemod.board.logic;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.world.board.*;

import java.util.*;

import static io.github.hato1883.basemod.BaseModMain.MOD_ID;
import static io.github.hato1883.basemod.board.logic.ClassicHexIslandBoard.DEFAULT_NUMBERS;
import static io.github.hato1883.basemod.board.logic.ClassicHexIslandBoard.DEFAULT_TILE_IDS;

public class DonutBoardType implements IBoardType {

    @Override public Identifier getIdentifier() { return Identifier.of(MOD_ID, "donut"); }
    @Override public String getName() { return "Donut Board"; }
    @Override public IShapeGenerator getShapeGenerator() { return new DonutShapeGenerator(); }
    @Override public BoardGenerationConfig getDefaultConfig() { return new BoardGenerationConfig(4, true); }

    private final List<Identifier> tilesLeftToPlace = new ArrayList<>(DEFAULT_TILE_IDS);
    private final List<Integer> tokensLeftToPlace = new ArrayList<>(DEFAULT_NUMBERS);

    @Override
    public List<ITilePosition> getTileOrder(Set<ITilePosition> coords, BoardGenerationConfig config, Random rng) {
        return coords.stream()
            .sorted(Comparator.comparingInt((tile) -> (int)((ITilePosition) tile).z())
                .thenComparing((tile) -> (int)((ITilePosition) tile).x())
                .thenComparing((tile) -> (int)((ITilePosition) tile).y()))
            .toList();
    }

    @Override
    public Optional<ITileType> chooseTile(ITilePosition position, BoardGenerationConfig config, Random rng) {
        if (tilesLeftToPlace.isEmpty())
            tilesLeftToPlace.addAll(DEFAULT_TILE_IDS);
        Identifier tileId = tilesLeftToPlace.removeFirst();
        return Optional.of(Registries.tiles().require(tileId));
    }

    @Override
    public Optional<Collection<Integer>> assignNumbers(ITilePosition position, BoardGenerationConfig config, Random rng) {
        if (tokensLeftToPlace.isEmpty())
            tokensLeftToPlace.addAll(DEFAULT_NUMBERS);
        Collection<Integer> tokens = new ArrayList<>();
        tokens.add(tokensLeftToPlace.removeFirst());
        return Optional.of(tokens);
    }

    @Override
    public ITileGrid getGrid() {
        return new HexGrid();
    }
}
