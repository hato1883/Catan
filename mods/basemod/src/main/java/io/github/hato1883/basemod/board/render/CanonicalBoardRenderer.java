package io.github.hato1883.basemod.board.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.hato1883.api.assets.AssetUpgradeNotifier;
import io.github.hato1883.api.assets.IAssetProvider;
import io.github.hato1883.api.assets.TextureUpgradeNotifier;
import io.github.hato1883.api.ui.model.IBoardView;
import io.github.hato1883.api.ui.model.RendererConfig;
import io.github.hato1883.api.ui.render.IBoardRenderer;
import io.github.hato1883.api.world.board.*;
import io.github.hato1883.api.ui.sprites.PolygonTileSprite;
import io.github.hato1883.api.ui.factories.PolygonSpriteFactory;

import java.util.*;

/**
 * Canonical board renderer for Catan using the new ITileGrid/ITile system.
 */
public class CanonicalBoardRenderer implements IBoardRenderer {
    private final RendererConfig config;
    private final PolygonSpriteFactory spriteFactory;
    private final Map<Integer, List<PolygonTileSprite>> lodToSprites = new HashMap<>();
    private List<PolygonTileSprite> currentSpriteList = Collections.emptyList();
    private int currentLod = -1;
    private IAssetProvider assets;
    private PolygonSpriteBatch polyBatch;
    private ShapeRenderer shapeRenderer;

    private IBoardView board; // Needed for LOD callback

    private final Set<Integer> availableLods = new HashSet<>();
    private BitmapFont numberFont;
    private SpriteBatch textBatch;
    private final GlyphLayout glyphLayout = new GlyphLayout();

    private Texture bandTexture;
    private TileBandRenderer bandRenderer;

    public CanonicalBoardRenderer(RendererConfig config) {
        this.config = config;
        this.spriteFactory = new PolygonSpriteFactory();
    }

    @Override
    public void initializeSprites(IAssetProvider assets, IBoardView board) {
        this.assets = assets;
        this.board = board; // Store for LOD callback
        if (polyBatch == null) {
            polyBatch = new PolygonSpriteBatch();
        }
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        if (numberFont == null) {
            // Use getFont from asset provider, and generate a large font (size 144) from Roboto-Regular.ttf
            try {
                // Try to get a font from the asset provider (should be Roboto-Regular.ttf)
                BitmapFont baseFont = assets.getFont("font/Roboto-Regular.ttf");
                if (baseFont != null) {
                    // Generate a large font using FreeTypeFontGenerator
                    FreeTypeFontGenerator generator =
                        new FreeTypeFontGenerator(
                            Gdx.files.internal("fonts/Roboto-Regular.ttf"));
                    FreeTypeFontGenerator.FreeTypeFontParameter param =
                        new FreeTypeFontGenerator.FreeTypeFontParameter();
                    param.size = 144;
                    param.minFilter = Texture.TextureFilter.Linear;
                    param.magFilter = Texture.TextureFilter.Linear;
                    numberFont = generator.generateFont(param);
                    generator.dispose();
                } else {
                    numberFont = new BitmapFont();
                }
            } catch (Exception e) {
                numberFont = new BitmapFont(); // fallback, but may be low-res
            }
        }
        if (textBatch == null) {
            textBatch = new SpriteBatch();
        }
        float fontScale = 0.10f; // Shrink text
        float bandPadding = config.tileRadius() * 0.05f; // Shrink band
        if (bandTexture == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(0, 0, 0, 0.4f); // semi-transparent red
            pixmap.fill();
            bandTexture = new Texture(pixmap);
            pixmap.dispose();
        }
        bandRenderer = new TileBandRenderer(bandTexture, bandPadding);
        // Only generate sprites for the lowest available LOD
        int lowestLod = 3;
        regenerateHexagonSpritesForLod(board, lowestLod);
        availableLods.clear();
        availableLods.add(lowestLod);
        currentLod = lowestLod;
        currentSpriteList = lodToSprites.get(currentLod);
    }

    @Override
    public void registerAssetUpgradeNotifier(AssetUpgradeNotifier notifier) {
        notifier.onAssetUpgrade(
            TextureUpgradeNotifier.class,
            (notifierObj, id, lod) -> {
                if (board != null) {
                    regenerateHexagonSpritesForLod(board, lod);
                    availableLods.add(lod);
                    // If the current LOD is now available, update the sprite list
                    if (lod == currentLod) {
                        currentSpriteList = lodToSprites.get(currentLod);
                    }
                }
            }
        );
    }

    private float getTileDiameter() {
        return 2f * config.tileRadius() + config.tileGap();
    }

    private void regenerateHexagonSpritesForLod(IBoardView board, int lod) {
        List<PolygonTileSprite> sprites = new ArrayList<>(board.getTiles().size());
        float diameter = getTileDiameter();
        float[] boardCenter = computeBoardPixelCenter(board, diameter);
        float centerX = boardCenter[0];
        float centerY = boardCenter[1];
        boolean debugPrinted = false;
        for (ITile tile : board.getTiles()) {
            ITileType type = tile.getType();
            TextureRegion region = assets.getTextureRegion(type.getId(), lod);
            String tileId = type.getId().toString();
            float regionWidth = region.getRegionWidth();
            float regionHeight = region.getRegionHeight();
            PolygonShape shape = board.getGrid().orElseGet(() -> board.getGridForTile(tile)).getPolygonShape(tile);
            PolygonShape scaledShape = createScaledShape(shape, regionWidth, regionHeight);

            // Unified scaling for all shapes
            float scale = config.tileRadius() / (regionWidth / 2f);

            float x = tile.getPosition().x() * diameter;
            float y = tile.getPosition().y() * diameter;
            float[] pixelPos = new float[] { x, y };
            float[] centroid = scaledShape.origin;
            if (!debugPrinted) {
                System.out.println("[DEBUG] LOD=" + lod + ", tile=" + tileId + ", regionSize=" + regionWidth + "x" + regionHeight + ", scale=" + scale + ", centroid=(" + centroid[0] + "," + centroid[1] + "), pixelPos=(" + pixelPos[0] + "," + pixelPos[1] + "), centerX=" + centerX + ", centerY=" + centerY);
                System.out.println("[DEBUG] Scaled shape vertices: " + Arrays.toString(scaledShape.vertices));
                debugPrinted = true;
            }
            PolygonTileSprite sprite = spriteFactory.create(tile, region, scaledShape);
            sprite.setScale(scale);
            applySpriteTransformations(sprite, pixelPos, scaledShape.origin, centerX, centerY);
            sprites.add(sprite);
        }
        lodToSprites.put(lod, sprites);
        if (lod == currentLod) {
            currentSpriteList = lodToSprites.get(currentLod);
        }
    }

    /**
     * Computes the average pixel position (center) of all tiles for centering the board.
     */
    private float[] computeBoardPixelCenter(IBoardView board, float diameter) {
        float sumX = 0, sumY = 0;
        int count = 0;
        for (ITile tile : board.getTiles()) {
            // Get the shape and its origin (normalized)
            ITileGrid grid = board.getGrid().orElseGet(() -> board.getGridForTile(tile));
            PolygonShape shape = grid.getPolygonShape(tile);
            float[] origin = shape.origin;
            // Compute the tile's world position (logical position * diameter)
            float baseX = tile.getPosition().x() * diameter;
            float baseY = tile.getPosition().y() * diameter;
            // Compute the shape's origin in world units (normalized origin * diameter)
            float originX = origin[0] * diameter;
            float originY = origin[1] * diameter;
            // The true rendered center is base + origin offset
            float centerX = baseX + (originX - diameter / 2f);
            float centerY = baseY + (originY - diameter / 2f);
            sumX += centerX;
            sumY += centerY;
            count++;
        }
        float avgX = (count > 0) ? sumX / count : 0;
        float avgY = (count > 0) ? sumY / count : 0;
        return new float[]{avgX, avgY};
    }

    /**
     * Scales the polygon vertices and computes the centroid for the origin.
     */
    private PolygonShape createScaledShape(PolygonShape shape, float regionWidth, float regionHeight) {
        float[] vertices = shape.vertices;
        float[] scaledVertices = new float[vertices.length];
        for (int i = 0; i < vertices.length; i += 2) {
            scaledVertices[i] = vertices[i] * regionWidth;
            scaledVertices[i + 1] = vertices[i + 1] * regionHeight;
        }
        float[] scaledOrigin = new float[]{shape.origin[0] * regionWidth, shape.origin[1] * regionHeight};
        return new PolygonShape(scaledVertices, scaledOrigin);
    }

    /**
     * Applies all translation logic to the sprite: position, centering, and board centering.
     * Does not modify the sprite's origin (important for rotation correctness).
     */
    private void applySpriteTransformations(PolygonTileSprite sprite, float[] pixelPos, float[] scaledOrigin, float centerX, float centerY) {
        sprite.setPosition(pixelPos[0], pixelPos[1]);
        sprite.translate(-scaledOrigin[0], -scaledOrigin[1]);
        if (centerX > Math.pow(1f, Float.PRECISION-10) || centerY > Math.pow(1f, Float.PRECISION-10)) {
            sprite.translate(-centerX, -centerY);
        }
    }

    @Override
    public void render(OrthographicCamera camera, IBoardView board, IAssetProvider assets) {
        ensureBatchesAndFont();
        int bestLod = selectBestLod(camera.zoom);
        updateCurrentLod(bestLod);
        renderHexTiles(camera);
        renderNumberTokens(camera, board);
    }

    private void ensureBatchesAndFont() {
        if (polyBatch == null) {
            polyBatch = new PolygonSpriteBatch();
        }
        if (numberFont == null) {
            numberFont = new BitmapFont();
        }
        if (textBatch == null) {
            textBatch = new SpriteBatch();
        }
    }

    private int selectBestLod(float zoom) {
        int bestLod = lodFromZoom(zoom);
        while (!availableLods.contains(bestLod) && bestLod < 3) {
            bestLod++;
        }
        if (!availableLods.contains(bestLod)) {
            bestLod = availableLods.stream().min(Integer::compareTo).orElse(3);
        }
        return bestLod;
    }

    private void updateCurrentLod(int bestLod) {
        if (bestLod != currentLod) {
            currentLod = bestLod;
            currentSpriteList = lodToSprites.getOrDefault(currentLod, Collections.emptyList());
        }
    }

    private void renderHexTiles(OrthographicCamera camera) {
        polyBatch.setProjectionMatrix(camera.combined);
        polyBatch.begin();
        for (PolygonTileSprite s : currentSpriteList) {
            s.draw(polyBatch);
        }
        polyBatch.end();
        polyBatch.setShader(null); // Reset to default
    }

    /**
     * Clips the given polygon (hexVerts, CCW, world coordinates) to the horizontal band yMin..yMax.
     * Returns a float[] of the resulting polygon (may have 4, 6, or 8 vertices).
     */
    public static float[] clipPolygonToBand(float[] verts, float yMin, float yMax) {
        // First clip to yMin (bottom), then to yMax (top)
        List<float[]> input = new ArrayList<>();
        for (int i = 0; i < verts.length; i += 2) input.add(new float[]{verts[i], verts[i+1]});
        input = clipPolygonToY(input, yMin, true);  // bottom
        input = clipPolygonToY(input, yMax, false); // top
        float[] out = new float[input.size() * 2];
        for (int i = 0; i < input.size(); ++i) {
            out[2*i] = input.get(i)[0];
            out[2*i+1] = input.get(i)[1];
        }
        return out;
    }

    // Sutherland–Hodgman style clipper for a single Y boundary
    private static List<float[]> clipPolygonToY(List<float[]> poly, float yClip, boolean isBottom) {
        List<float[]> out = new ArrayList<>();
        int n = poly.size();
        for (int i = 0; i < n; ++i) {
            float[] a = poly.get(i);
            float[] b = poly.get((i+1)%n);
            boolean aIn = isBottom ? a[1] >= yClip : a[1] <= yClip;
            boolean bIn = isBottom ? b[1] >= yClip : b[1] <= yClip;
            if (aIn && bIn) {
                out.add(b);
            } else if (aIn && !bIn) {
                // a in, b out: add intersection
                float t = (yClip - a[1]) / (b[1] - a[1]);
                float x = a[0] + t * (b[0] - a[0]);
                out.add(new float[]{x, yClip});
            } else if (!aIn && bIn) {
                // a out, b in: add intersection and b
                float t = (yClip - a[1]) / (b[1] - a[1]);
                float x = a[0] + t * (b[0] - a[0]);
                out.add(new float[]{x, yClip});
                out.add(b);
            }
        }
        return out;
    }

    private void renderNumberTokens(OrthographicCamera camera, IBoardView board) {
        float diameter = getTileDiameter();
        float[] boardCenter = computeBoardPixelCenter(board, diameter);
        float centerX = boardCenter[0];
        float centerY = boardCenter[1];
        float tileRadius = config.tileRadius();
        float tileGap = config.tileGap();
        float fontScale = 0.10f; // Shrink text
        float prevFontScaleX = numberFont.getData().scaleX;
        float prevFontScaleY = numberFont.getData().scaleY;
        numberFont.getData().setScale(fontScale);
        // Draw filled band polygons using TileBandRenderer
        polyBatch.begin();
        bandRenderer.drawBands(polyBatch, board, tileRadius, tileGap, boardCenter, numberFont);
        polyBatch.end();
        // Draw text as before
        textBatch.setProjectionMatrix(camera.combined);
        textBatch.begin();
        for (ITile tile : board.getTiles()) {
            float x = tile.getPosition().x() * diameter - centerX;
            float y = tile.getPosition().y() * diameter - centerY;
            Collection<Integer> numbers = null;
            try {
                numbers = tile.getProductionNumbers();
            } catch (Exception ignored) {}
            if (numbers != null && !numbers.isEmpty()) {
                String text = numbers.stream().map(String::valueOf).reduce((a, b) -> a + ", " + b).orElse("");
                glyphLayout.setText(numberFont, text);
                float textWidth = glyphLayout.width;
                float textHeight = glyphLayout.height;
                float bandHeight = textHeight + tileRadius * 0.05f;
                PolygonShape shape = board.getGrid().orElseGet(() -> board.getGridForTile(tile)).getPolygonShape(tile);
                float[] verts = shape.vertices;
                float[] origin = shape.origin;
                // Use canonical vertex order: verts[0,1]=A, verts[2,3]=B, verts[4,5]=C
                // For up triangle: tip is C (verts[4,5]), for down: tip is C (verts[4,5])
                boolean up = Math.abs(tile.getPosition().getYaw()) < 1e-3 || Math.abs(tile.getPosition().getYaw() - 360f) < 1e-3;
                int tipIdx = 4; // always C
                float[] tip = { verts[tipIdx], verts[tipIdx+1] };
                float[] centroid = { origin[0], origin[1] };
                // Compute base midpoint (A and B)
                float baseMidX = (verts[0] + verts[2]) / 2f;
                float baseMidY = (verts[1] + verts[3]) / 2f;
                float bandFrac = 0.6f;
                float bandCenterX, bandCenterY;
                if (verts.length == 6) { // triangle
                    baseMidX = (verts[0] + verts[2]) / 2f;
                    baseMidY = (verts[1] + verts[3]) / 2f;
                    bandFrac = 0.6f;
                    bandCenterX = centroid[0] + bandFrac * (baseMidX - centroid[0]);
                    bandCenterY = centroid[1] + bandFrac * (baseMidY - centroid[1]);
                } else { // hexagon or other
                    bandCenterX = centroid[0];
                    bandCenterY = centroid[1];
                }
                float scale = tileRadius * 2;
                float bandCenterWorldX = bandCenterX * scale + x - origin[0] * scale;
                float bandCenterWorldY = bandCenterY * scale + y - origin[1] * scale;
                float textX = bandCenterWorldX - textWidth / 2f;
                float textY = bandCenterWorldY + textHeight / 2f;
                numberFont.draw(textBatch, glyphLayout, textX, textY);
            }
        }
        textBatch.end();
        numberFont.getData().setScale(prevFontScaleX, prevFontScaleY);
    }

    @Override
    public void dispose() {
        if (polyBatch != null) {
            polyBatch.dispose();
            polyBatch = null;
        }
        if (textBatch != null) {
            textBatch.dispose();
            textBatch = null;
        }
        if (numberFont != null) {
            numberFont.dispose();
            numberFont = null;
        }
        if (bandTexture != null) {
            bandTexture.dispose();
            bandTexture = null;
        }
        if (bandRenderer != null) bandRenderer.clearCache();
    }

    private int lodFromZoom(float zoom) {
        // Select LOD based on the tile's height in screen pixels after zoom
        if (zoom <= 0) return 3; // fallback to lowest LOD
        float tileWorldHeight = 2f * config.tileRadius();
        float tilePixelHeight = tileWorldHeight / zoom;
        if (tilePixelHeight > 256) return 0;
        else if (tilePixelHeight > 128) return 1;
        else if (tilePixelHeight > 64)  return 2;
        else return 3;
    }

    /**
     * Triangulate a convex polygon for use with PolygonRegion.
     * Returns a short[] of triangle indices (fan triangulation).
     */
    public static short[] triangulateBand(float[] verts) {
        int n = verts.length / 2;
        short[] triangles = new short[(n - 2) * 3];
        for (int i = 0; i < n - 2; i++) {
            triangles[i * 3] = 0;
            triangles[i * 3 + 1] = (short) (i + 1);
            triangles[i * 3 + 2] = (short) (i + 2);
        }
        return triangles;
    }
}
