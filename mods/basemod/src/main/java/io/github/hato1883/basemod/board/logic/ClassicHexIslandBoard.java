package io.github.hato1883.basemod.board.logic;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.world.board.*;

import java.util.*;

import static io.github.hato1883.basemod.BaseModMain.MOD_ID;

public class ClassicHexIslandBoard implements IBoardType {

    static final List<Identifier> DEFAULT_TILE_IDS = List.of(
        Identifier.of(MOD_ID, "mountain"),    // top-left corner
        Identifier.of(MOD_ID, "field"),
        Identifier.of(MOD_ID, "pasture"),
        Identifier.of(MOD_ID, "hill"),
        Identifier.of(MOD_ID, "field"),
        Identifier.of(MOD_ID, "mountain"),
        Identifier.of(MOD_ID, "forest"),
        Identifier.of(MOD_ID, "forest"),
        Identifier.of(MOD_ID, "field"),
        Identifier.of(MOD_ID, "pasture"),
        Identifier.of(MOD_ID, "hill"),
        Identifier.of(MOD_ID, "forest"),
        Identifier.of(MOD_ID, "mountain"),
        Identifier.of(MOD_ID, "forest"),
        Identifier.of(MOD_ID, "pasture"),
        Identifier.of(MOD_ID, "pasture"),
        Identifier.of(MOD_ID, "field"),
        Identifier.of(MOD_ID, "hill"),
        Identifier.of(MOD_ID, "desert")
    );

    static final List<Integer> DEFAULT_NUMBERS = List.of(
        5, 2, 6, 3, 8, 10, 9, 12, 11,
        4, 8, 10, 9, 4, 5, 6, 3, 11
    );

    private final List<Identifier> tilesLeftToPlace = new ArrayList<>(DEFAULT_TILE_IDS);
    private final List<Integer> tokensLeftToPlace = new ArrayList<>(DEFAULT_NUMBERS);

    private Identifier id;
    private String name;

    public ClassicHexIslandBoard() {
        id = Identifier.of(MOD_ID, "classic_hex");
        name = "Classic Hex Island";
    }

    protected ClassicHexIslandBoard(Identifier id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Identifier getIdentifier() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public IShapeGenerator getShapeGenerator() {
        return new ClassicHexShapeGenerator();
    }

    @Override
    public BoardGenerationConfig getDefaultConfig() {
        // Disable shuffling by default
        return new BoardGenerationConfig(3, false);
    }

    @Override
    public List<ITilePosition> getTileOrder(Set<ITilePosition> coords, BoardGenerationConfig config, Random rng) {
        return coords.stream()
            .sorted(Comparator.comparingInt((tile) -> (int)((ITilePosition) tile).z())
                .thenComparing((tile) -> (int)((ITilePosition) tile).x())
                .thenComparing((tile) -> (int)((ITilePosition) tile).y()))
            .toList();
        //return SpiralUtils.spiralOrder(coords, config.shouldShuffleTiles() ? rng : null); // default ordering
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

// Registration
}
