package io.github.hato1883.game.board;

import io.github.hato1883.game.resource.ResourceType;
/**
 * A factory interface for creating {@link HexTile} instances with specified attributes.
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
 *     <li>{@code resource} - The {@link ResourceType} this tile will produce</li>
 *     <li>{@code numberToken} - The dice number token value (0 for desert)</li>
 *     <li>{@code coord} - The {@link CubeCoord} position on the game board</li>
 * </ul>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link HexTile} - The product this factory creates</li>
 *     <li>{@link Board#generateTiles()} - Where this factory is typically used</li>
 *     <li>{@link CubeCoord} - The coordinate system used for tile placement</li>
 * </ul>
 */
public interface HexTileFactory {
    /**
     * Creates a new {@link HexTile} instance with the specified attributes.
     *
     * @param resource the resource type this tile will produce
     * @param numberToken the dice number token value (2-12, 0 for desert)
     * @param coord the hexagonal coordinate position
     * @return a fully configured {@link HexTile} instance
     *
     * @throws IllegalArgumentException if coordinates are invalid for the board layout
     *
     * <h3>Implementation Notes:</h3>
     * Implementations should ensure:
     * <ul>
     *     <li>The returned tile is non-null</li>
     *     <li>Number tokens are valid (0 or 2-12)</li>
     *     <li>Coordinates are properly validated</li>
     * </ul>
     */
    HexTile createTile(ResourceType resource, int numberToken, CubeCoord coord);
}
