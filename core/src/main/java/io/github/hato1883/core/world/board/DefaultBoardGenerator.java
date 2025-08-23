package io.github.hato1883.core.world.board;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.registries.IBoardTypeRegistry;
import io.github.hato1883.api.world.board.*;
import java.util.*;

/**
 * Default implementation of IBoardGenerator.
 * Uses IShapeGenerator and IBoardType for customizable board generation.
 */
public class DefaultBoardGenerator implements IBoardGenerator {

    private final IBoardTypeRegistry boardRegistry;

    public DefaultBoardGenerator(IBoardTypeRegistry boardRegistry) {
        this.boardRegistry = boardRegistry;
    }

    @Override
    public IBoard generateBoard(IBoardType type, BoardGenerationConfig config, Random rng) {
        if (config == null) config = type.getDefaultConfig();
        if (rng == null) rng = new Random();
        // Pre-generation event (if any)
        // EventBus.post(new PreBoardGenerationEvent(type, config));
        IShapeGenerator shapeGen = type.getShapeGenerator();
        Set<ITilePosition> positions = shapeGen.generateCoords(config, rng);
        List<ITilePosition> ordered = type.getTileOrder(positions, config, rng);
        IBoard board = new AbstractCatanBoard(type.getGrid()) {};
        for (ITilePosition pos : ordered) {
            Optional<ITileType> tileTypeOpt = type.chooseTile(pos, config, rng);
            Optional<Collection<Integer>> numbersOpt = type.assignNumbers(pos, config, rng);
            if (tileTypeOpt.isPresent()) {
                ITileType tileType = tileTypeOpt.get();
                Collection<Integer> numbers = numbersOpt.orElse(Collections.emptyList());
                ITile tile = new ITile.DefaultTile(tileType, pos, numbers);
                board.addTile(tile);
            }
        }
        // Post-generation event (if any)
        // EventBus.post(new PostBoardGenerationEvent(board));
        return board;
    }

    @Override
    public IBoard generateBoard(Identifier id, BoardGenerationConfig config, Random rng) {
        IBoardType type = boardRegistry.require(id);
        return generateBoard(type, config, rng);
    }

    @Override
    public IBoard generateBoard(Identifier id, Random rng) {
        return generateBoard(id, null, rng);
    }
}
