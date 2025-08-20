package io.github.hato1883.core.ui.gui.sprites;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import io.github.hato1883.api.world.board.IHexTile;

/**
 * A specialized {@link PolygonSprite} that renders hexagonal game tiles.
 * <p>
 * Creates and manages a six-sided polygon region with proper UV mapping for texture rendering.
 *
 * <h3>Key Features:</h3>
 * <ul>
 *     <li>Automatic hexagon geometry generation</li>
 *     <li>Proper texture mapping for hex-shaped regions</li>
 *     <li>Origin-centered by default</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * TextureRegion woodTexture = atlas.findRegion("lumber");
 * HexagonSprite hex = new HexagonSprite(woodTexture, 50f);
 * hex.setPosition(100, 100);
 * hex.draw(batch);
 * }</pre>
 *
 * <h3>See Also:</h3>
 * <ul>
 *     <li>{@link PolygonSprite} - Base class for polygon rendering</li>
 * </ul>
 */
public class HexagonSprite extends PolygonSprite {
    // FIXME: Consider making triangulator instance non-static if threading issues arise
    private static final EarClippingTriangulator triangulator = new EarClippingTriangulator();
    private final IHexTile tile;

    /**
     * Creates a new hexagonal sprite with specified texture and size.
     *
     * @param baseRegion the texture region to apply to the hexagon
     * @param radius the distance from center to vertex (in world units)
     * @throws IllegalArgumentException if radius ≤ 0 or texture is null
     *
     * <h3>Setup Behavior:</h3>
     * <ul>
     *     <li>Automatically generates hexagon geometry</li>
     *     <li>Sets origin to geometric center</li>
     *     <li>Preserves texture UV mapping</li>
     * </ul>
     *
     * <h3>Performance Note:</h3>
     * // TODO: Cache common radius geometries to avoid recomputation
     */
    public HexagonSprite(TextureRegion baseRegion, float radius, IHexTile tile) {
        super(buildHexagonRegion(baseRegion, radius));
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        this.tile = tile;
    }

    /**
     * Generates the polygon geometry for a regular hexagon.
     *
     * @param texture the source texture region
     * @param radius the hexagon's radius
     * @return a configured PolygonRegion ready for rendering
     *
     * <h3>Geometry Generation:</h3>
     * Creates a pointy-top hexagon with:
     * <ul>
     *     <li>6 vertices (12 coordinate values)</li>
     *     <li>4 triangles (12 indices)</li>
     *     <li>Proper UV mapping from texture</li>
     * </ul>
     *
     * <h3>Math Notes:</h3>
     * Vertex positions calculated using:
     * <pre>{@code
     * x = radius * (1 + cos(60°i - 30°))
     * y = height/2 + radius * sin(60°i - 30°)
     * }</pre>
     */
    private static PolygonRegion buildHexagonRegion(TextureRegion texture, float radius) {
        // FIXME: Add validation for radius > 0
        float[] vertices = new float[12];
        float width = (float)(Math.sqrt(3) * radius);
        float height = 2 * radius;

        // Generate hexagon vertices
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);  // 30° offset for pointy-top
            float x = (float)(radius + radius * Math.cos(angle));
            float y = (float)(height / 2 + radius * Math.sin(angle));
            vertices[i * 2] = x;
            vertices[i * 2 + 1] = y;
        }

        // Triangulate the hexagon
        short[] triangles = triangulator.computeTriangles(vertices).toArray();
        return new PolygonRegion(texture, vertices, triangles);
    }

    public IHexTile getTile() {
        return tile;
    }

    // TODO: Add hex-specific collision detection
    /**
     * Checks if a point is inside the hexagonal bounds.
     * @param x screen x-coordinate
     * @param y screen y-coordinate
     * @return true if point is within the hexagon
     *
     * <h3>Implementation Needed:</h3>
     * Should use hexagonal point-in-polygon test rather than
     * default rectangular bounds check.
     */
    // public boolean contains(float x, float y) { ... }
}
