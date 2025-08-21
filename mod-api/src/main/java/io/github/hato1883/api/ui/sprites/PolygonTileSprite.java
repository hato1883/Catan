package io.github.hato1883.api.ui.sprites;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.PolygonShape;

import java.util.Arrays;

/**
 * A general polygon sprite for rendering any tile shape.
 */
public class PolygonTileSprite extends PolygonSprite {
    private static final EarClippingTriangulator triangulator = new EarClippingTriangulator();
    private final ITile tile;

    /**
     * @param texture The texture region to use
     * @param shape The polygon shape (vertices in [0,1], origin in [0,1])
     * @param tile The tile this sprite represents
     */
    public PolygonTileSprite(TextureRegion texture, PolygonShape shape, ITile tile) {
        super(createPolygonRegion(texture, shape.vertices));
        this.tile = tile;
        setOrigin(shape.origin[0], shape.origin[1]);
    }

    public ITile getTile() {
        return tile;
    }

    private static PolygonRegion createPolygonRegion(TextureRegion texture, float[] vertices) {
        short[] triangles = triangulator.computeTriangles(vertices).toArray();
        return new PolygonRegion(texture, vertices, triangles);
    }

    @Override
    public String toString() {
        return "PolygonTileSprite{" +
                "tile=" + tile +
                ", vertices=" + Arrays.toString(getVertices()) +
                ", texture=" + getRegion().getRegion().getTexture() +
                ", origin=(" + getOriginX() + ", " + getOriginY() + ")" +
                '}';
    }
}
