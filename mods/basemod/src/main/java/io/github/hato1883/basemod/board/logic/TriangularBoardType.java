package io.github.hato1883.basemod.board.logic;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.world.board.*;
import java.util.*;
import static io.github.hato1883.basemod.BaseModMain.MOD_ID;
import static io.github.hato1883.basemod.board.logic.ClassicHexIslandBoard.DEFAULT_NUMBERS;
import static io.github.hato1883.basemod.board.logic.ClassicHexIslandBoard.DEFAULT_TILE_IDS;

public class TriangularBoardType implements IBoardType {
    private final Identifier id = Identifier.of(MOD_ID, "triangular_grid");
    private final String name = "Triangular Grid Board";
    private final IShapeGenerator shapeGenerator = new TriangularBoardHexTilingGenerator();

    private final List<Identifier> tilesLeftToPlace = new ArrayList<>(DEFAULT_TILE_IDS);
    private final List<Integer> tokensLeftToPlace = new ArrayList<>(DEFAULT_NUMBERS);

    @Override
    public Identifier getIdentifier() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public IShapeGenerator getShapeGenerator() { return shapeGenerator; }

    public BoardGenerationConfig getDefaultConfig() {
        // Default: 5x5 grid, no shuffle
        return new BoardGenerationConfig(5, 5, 5, false);
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
        return new TriangularGrid();
    }
}
