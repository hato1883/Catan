package io.github.hato1883.core.game.board;

import io.github.hato1883.api.game.IResourceType;
import io.github.hato1883.api.game.board.IBoardType;
import io.github.hato1883.api.game.board.ICubeCoord;
import io.github.hato1883.api.game.board.IHexTile;
import io.github.hato1883.api.game.board.ITileType;

import java.util.Collection;

/**
 * A factory interface for creating {@link HexTileImpl} instances with specified attributes.
 * <p>
 * This factory pattern allows for flexible creation of hex tiles while maintaining control over
 * the instantiation process. Implementations can customize tile creation for different game scenarios.
 *
 * <h3>Typical Usage:</h3>
 * Used during board generation to create tiles with specific resources, number tokens, and coordinates.
 *
 * <h3>Example Implementation:</h3>
 * <pre>{@code
 * HexTileFactory factory = (resource, token, coord) ->
 *     new HexTile(resource, token, coord);
 * }</pre>
 *
 * <h3>Method Parameters:</h3>
 * <ul>
 *     <li>{@code resource} - The {@link IResourceType} this tile will produce</li>
 *     <li>{@code numberToken} - The dice number token value (0 for desert)</li>
 *     <li>{@code coord} - The {@link ICubeCoord} position on the game board</li>
 * </ul>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link HexTileImpl} - The product this factory creates</li>
 *     <li>{@link IBoardType#generateTiles()} - Where this factory is typically used</li>
 *     <li>{@link ICubeCoord} - The coordinate system used for tile placement</li>
 * </ul>
 */
public class HexTileFactoryImpl {

    public static IHexTile createTile(
        ITileType tileType,
        ICubeCoord coord,
        Collection<Integer> productionNumbers
    ) {
        return new HexTileImpl(tileType, coord, productionNumbers);
    }
}
