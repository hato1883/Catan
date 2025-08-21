package io.github.hato1883.core.factories;

import io.github.hato1883.api.entities.resource.IResourceType;
import io.github.hato1883.api.world.board.IBoardType;
import io.github.hato1883.api.world.board.ITilePosition;
import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.ITileType;
import io.github.hato1883.core.game.world.board.Tile;

import java.util.Collection;

/**
 * A factory interface for creating {@link Tile} instances with specified attributes.
 * <p>
 * This factory pattern allows for flexible creation of tiles while maintaining control over
 * the instantiation process. Implementations can customize tile creation for different game scenarios.
 *
 * <h3>Typical Usage:</h3>
 * Used during board generation to create tiles with specific resources, number tokens, and coordinates.
 *
 * <h3>Example Implementation:</h3>
 * <pre>{@code
 * TileFactory factory = (resource, token, coord) ->
 *     new Tile(resource, token, coord);
 * }</pre>
 *
 * <h3>Method Parameters:</h3>
 * <ul>
 *     <li>{@code resource} - The {@link IResourceType} this tile will produce</li>
 *     <li>{@code numberToken} - The dice number token value (0 for desert)</li>
 *     <li>{@code coord} - The {@link ITilePosition} position on the game board</li>
 * </ul>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link Tile} - The product this factory creates</li>
 *     <li>{@link IBoardType#generateTiles()} - Where this factory is typically used</li>
 *     <li>{@link ITilePosition} - The coordinate system used for tile placement</li>
 * </ul>
 */
public class TileFactory {

    public static ITile createTile(
        ITileType tileType,
        ITilePosition position,
        Collection<Integer> productionNumbers
    ) {
        return new Tile(tileType, position, productionNumbers);
    }
}

