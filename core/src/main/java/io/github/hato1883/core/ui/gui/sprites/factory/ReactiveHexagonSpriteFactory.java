package io.github.hato1883.core.ui.gui.sprites.factory;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.game.board.ICubeCoord;
import io.github.hato1883.api.game.board.IHexTile;
import io.github.hato1883.core.assets.textures.TileTextureProvider;
import io.github.hato1883.core.ui.gui.sprites.HexagonSprite;

import java.util.function.Consumer;

/**
 * Factory that creates ReactiveHexagonSprites whose textures can be upgraded
 * automatically when higher-resolution LODs are loaded.
 */
public class ReactiveHexagonSpriteFactory {

    private final HexagonSpriteFactory baseFactory;
    private final TileTextureProvider textureProvider;

    /**
     * @param baseFactory Existing factory responsible for geometry/positioning
     * @param textureProvider Provider that can give TextureRegion for a tile and LOD
     */
    public ReactiveHexagonSpriteFactory(HexagonSpriteFactory baseFactory,
                                        TileTextureProvider textureProvider) {
        this.baseFactory = baseFactory;
        this.textureProvider = textureProvider;
    }

    /**
     * Create a sprite for a tile at a given LOD.
     *
     * If the texture is not yet loaded, a fallback texture will be used,
     * and the sprite will automatically update when the higher-resolution
     * texture becomes available.
     */
    public HexagonSprite create(IHexTile tile, int lod) {
        // Try to get primary texture
        TextureRegion region = textureProvider.getTileTexture(lod, tile.getTileType().getId());

        // Create sprite using base factory
        HexagonSprite sprite = baseFactory.create(tile, region);
        final float tileRadius = baseFactory.getTileRadius();
        final float tileGap = baseFactory.getTileGap();
        final ICubeCoord coord = tile.getCoord();
//        textureProvider.requestTexture(lod, tile.getTileType().getId(), new Consumer<TextureRegion>() {
//            @Override
//            public void accept(TextureRegion region) {
//                sprite.getRegion().getRegion().setRegion(region);
//                sprite.setScale(tileRadius * 2f / region.getRegionWidth());
//            }
//        });

        // Position the sprite in world coordinates
        float width = (float) (Math.sqrt(3) * tileRadius);
        float height = 2f * tileRadius;

        float xSpacing = width + tileGap;
        float ySpacing = height * 0.75f + tileGap;

        float worldX = xSpacing * (coord.x() + coord.y() / 2f);
        float worldY = ySpacing * -coord.y();

        sprite.setPosition(worldX - sprite.getOriginX(), worldY - sprite.getOriginY());

        return sprite;
    }
}


