package io.github.hato1883.api.ui.factories;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.PolygonShape;
import io.github.hato1883.api.ui.sprites.PolygonTileSprite;

/**
 * Factory for creating PolygonTileSprite for arbitrary polygonal tiles.
 */
public class PolygonSpriteFactory {
    public PolygonTileSprite create(ITile tile, TextureRegion texture, PolygonShape shape) {
        return new PolygonTileSprite(texture, shape, tile);
    }
}

