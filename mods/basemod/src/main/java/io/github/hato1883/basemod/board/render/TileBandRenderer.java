package io.github.hato1883.basemod.board.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.hato1883.api.ui.model.IBoardView;
import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.ITilePosition;
import io.github.hato1883.api.world.board.PolygonShape;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TileBandRenderer {
    private final Map<ITile, PolygonSprite> bandSprites = new HashMap<>();
    private final Texture bandTexture;
    private final float bandPadding;
    private final GlyphLayout glyphLayout = new GlyphLayout();

    public TileBandRenderer(Texture bandTexture, float bandPadding) {
        this.bandTexture = bandTexture;
        this.bandPadding = bandPadding;
    }

    public void drawBands(PolygonSpriteBatch polyBatch, IBoardView board, float tileRadius, float tileGap, float[] boardCenter, BitmapFont numberFont) {
        float spacingRadius = tileRadius * 2 + tileGap;
        float centerX = boardCenter[0];
        float centerY = boardCenter[1];
        for (ITile tile : board.getTiles()) {
            ITilePosition pixelPos = tile.getPosition();
            float x = pixelPos.x() * spacingRadius - centerX;
            float y = pixelPos.y() * spacingRadius - centerY;
            Collection<Integer> numbers = null;
            try { numbers = tile.getProductionNumbers(); } catch (Exception ignored) {}
            if (numbers != null && !numbers.isEmpty()) {
                glyphLayout.setText(numberFont, numbers.stream().map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse(""));
                float textHeight = glyphLayout.height;
                float bandHeight = textHeight + bandPadding;
                PolygonShape shape = board.getGrid().orElseGet(() -> board.getGridForTile(tile)).getPolygonShape(tile);
                float[] verts = shape.vertices;
                float[] origin = shape.origin;
                float[] centroid = { origin[0], origin[1] };
                float bandCenterX, bandCenterY;
                if (verts.length == 6) { // triangle
                    float baseMidX = (verts[0] + verts[2]) / 2f;
                    float baseMidY = (verts[1] + verts[3]) / 2f;
                    float bandFrac = 0.6f;
                    bandCenterX = centroid[0] + bandFrac * (baseMidX - centroid[0]);
                    bandCenterY = centroid[1] + bandFrac * (baseMidY - centroid[1]);
                } else { // hexagon or other
                    bandCenterX = centroid[0];
                    bandCenterY = centroid[1];
                }
                float scale = tileRadius * 2;
                float bandCenterWorldY = bandCenterY * scale + y - origin[1] * scale;
                float[] triVerts = new float[verts.length];
                for (int i = 0; i < verts.length; i += 2) {
                    triVerts[i] = verts[i] * scale + x - origin[0] * scale;
                    triVerts[i+1] = verts[i+1] * scale + y - origin[1] * scale;
                }
                float bandBottom = bandCenterWorldY - bandHeight / 2f;
                float bandTop = bandCenterWorldY + bandHeight / 2f;
                float[] bandPoly = CanonicalBoardRenderer.clipPolygonToBand(triVerts, bandBottom, bandTop);
                if (bandPoly.length >= 6) {
                    PolygonSprite bandSprite = bandSprites.get(tile);
                    boolean needsUpdate = bandSprite == null || bandSprite.getVertices().length != bandPoly.length;
                    if (needsUpdate) {
                        PolygonRegion region = new PolygonRegion(new TextureRegion(bandTexture), bandPoly, CanonicalBoardRenderer.triangulateBand(bandPoly));
                        bandSprite = new PolygonSprite(region);
                        bandSprites.put(tile, bandSprite);
                    } else {
                        bandSprite.setPosition(0, 0);
                    }
                    bandSprite.draw(polyBatch);
                }
            }
        }
    }

    public void clearCache() {
        bandSprites.clear();
    }
}
