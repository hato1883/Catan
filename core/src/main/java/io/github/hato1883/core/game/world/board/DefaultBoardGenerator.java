package io.github.hato1883.core.game.world.board;

import io.github.hato1883.api.Events;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.Services;
import io.github.hato1883.api.Registries;
import io.github.hato1883.api.events.board.PostBoardGenerationEvent;
import io.github.hato1883.api.events.board.PreBoardGenerationEvent;
import io.github.hato1883.api.world.board.*;
import io.github.hato1883.core.factories.HexTileFactory;

import java.util.*;

public class DefaultBoardGenerator implements IBoardGenerator {

    @Override
    public IBoard generateBoard(IBoardType type, BoardGenerationConfig config, Random rng) {
        IBoard board = new GenericBoard(
            type.getName(),
            new Dimension(config.getxExtent(), config.getyExtent(), config.getzExtent())
        );

        // Pre-generation event
        Events.post(new PreBoardGenerationEvent(board, config, rng));

        // Coordinates + placement
        Set<ICubeCoord> coords = type.getShapeGenerator().generateCoords(config, rng);
        List<ICubeCoord> placementOrder = type.getCubeOrder(coords, config, rng);

        // Tiles + numbers
        for (ICubeCoord coord : placementOrder) {
            type.chooseTile(coord, config, rng).ifPresent(tileType -> {
                Collection<Integer> numbers = type.assignNumbers(coord, config, rng)
                    .orElseGet(() -> getProductionNumbers(tileType, rng));

                IHexTile tile = HexTileFactory.createTile(tileType, coord, numbers);
                board.addTile(tile);
            });
        }

        // Post-generation event
        Events.post(new PostBoardGenerationEvent(board, config, rng));

        return board;
    }

    @Override
    public IBoard generateBoard(Identifier id, Random rng) {
        IBoardType type = getBoardTypeOrThrow(id);
        IBoardGenerator generator = Services.require(IBoardGenerator.class); // or DI
        return generator.generateBoard(type, type.getDefaultConfig(), rng);
    }

    @Override
    public IBoard generateBoard(Identifier id, BoardGenerationConfig config, Random rng) {
        IBoardType type = getBoardTypeOrThrow(id);
        IBoardGenerator generator = Services.require(IBoardGenerator.class); // or DI
        return generator.generateBoard(type, config, rng);
    }

    private IBoardType getBoardTypeOrThrow(Identifier id) {
        return Registries.boards().get(id)
            .orElseThrow(() -> new IllegalArgumentException("Unknown board type: " + id));
    }

    private Collection<Integer> getProductionNumbers(ITileType tileType, Random rng) {
        List<Integer> numbers = List.of(2, 3, 4, 5, 6, 8, 9, 10, 11, 12);
        return List.of(numbers.get(rng.nextInt(numbers.size())));
    }
}

