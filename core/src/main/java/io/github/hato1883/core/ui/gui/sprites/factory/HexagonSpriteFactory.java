package io.github.hato1883.core.ui.gui.sprites.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.game.board.ICubeCoord;
import io.github.hato1883.api.game.board.IHexTile;
import io.github.hato1883.core.ui.gui.sprites.HexagonSprite;

public final class HexagonSpriteFactory {
    private final float tileRadius;
    private final float tileGap;

    public HexagonSpriteFactory(float tileRadius, float tileGap) {
        this.tileRadius = tileRadius;
        this.tileGap = tileGap;
    }

    public HexagonSprite create(IHexTile tile, TextureRegion texture) {
        ICubeCoord coord = tile.getCoord();

        HexagonSprite sprite = new HexagonSprite(texture, texture.getRegionWidth() / 2f, tile);
        sprite.setScale(tileRadius * 2f / texture.getRegionWidth());

        float width = (float) (Math.sqrt(3) * tileRadius);
        float height = 2f * tileRadius;

        float xSpacing = width + tileGap;
        float ySpacing = height * 0.75f + tileGap;

        float worldX = xSpacing * (coord.x() + coord.y() / 2f);
        float worldY = ySpacing * -coord.y();

        sprite.setPosition(worldX - sprite.getOriginX(), worldY - sprite.getOriginY());
        return sprite;
    }

    public float getTileGap() {
        return tileGap;
    }

    public float getTileRadius() {
        return tileRadius;
    }
}
