package io.github.hato1883.core.ui.gui.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.assets.TextureUpgradeCallback;
import io.github.hato1883.api.assets.TextureUpgradeNotifier;
import io.github.hato1883.api.ui.factories.PolygonSpriteFactory;
import io.github.hato1883.api.ui.sprites.PolygonTileSprite;
import io.github.hato1883.api.world.board.ITile;
import io.github.hato1883.api.world.board.ITileType;
import io.github.hato1883.api.world.board.PolygonShape;
import io.github.hato1883.api.ui.model.IBoardView;
import io.github.hato1883.api.world.board.ITileGrid;
import io.github.hato1883.core.assets.management.loaders.LoadedAssets;
import io.github.hato1883.core.config.RendererConfig;
import io.github.hato1883.core.ui.gui.sprites.NumberTokenSprite;
import io.github.hato1883.core.assets.management.textures.TileTextureProvider;

import java.util.*;

/**
 * Renders tiles and tokens. No asset loading, no input, no model mutation.
 */
public final class BoardRenderer {
    private final IBoardView board;
    private final RendererConfig config;
    private final PolygonSpriteFactory spriteFactory;

    private final PolygonSpriteBatch polyBatch = new PolygonSpriteBatch();
    private final SpriteBatch spriteBatch = new SpriteBatch();

    private final TextureUpgradeCallback<TileTextureProvider> upgradeCallback = (TileTextureProvider provider, Identifier id, int lod) -> {
        LogManager.getLogger("BoardRenderer").info("Upgrading texture for {} at lod {}", id, lod);
        for (int i = 0; i < 1_000_000; i++) {}
        // rebuild sprites (but don't re-register the callback)
        regenerateHexagonSpritesForLod(provider, lod);
    };

    // LOD cache
    private final Map<Integer, List<PolygonTileSprite>> lodToSprites = new HashMap<>();
    private final List<NumberTokenSprite> tokenSprites = new ArrayList<>();
    private List<PolygonTileSprite> currentSpriteList = Collections.emptyList();
    private int currentLod = -1;

    public BoardRenderer(IBoardView board, RendererConfig config) {
        this.board = board;
        this.config = config;
        this.spriteFactory = new PolygonSpriteFactory();
    }

    public void initializeSprites(LoadedAssets assets) {
        TileTextureProvider textures = assets.tileTextures();
        // Register upgrade callback using the notifier interface
        if (textures instanceof TextureUpgradeNotifier notifier) {
            notifier.onTextureUpgrade(upgradeCallback);
        }

        // Rebuild hex tiles (all LODs)
        generateHexagonSprites(textures);

        // Rebuild number tokens (LOD0 only)
        generateNumberTokenSprites(assets.numberTokenFont());
    }

    private void generateHexagonSprites(TileTextureProvider textures) {
        // Delete previous tiles to avoid memory leaks
        lodToSprites.clear();
        // Initialize current lod to the highest quality
        currentLod = 0;

        // Generate tiles for each lod
        for (int lod = 3; lod >= 0; lod--) {
            regenerateHexagonSpritesForLod(textures, lod);
        }
    }

    private void generateNumberTokenSprites(BitmapFont font) {
//        NumberTokenFactory tokenFactory = new NumberTokenFactory();
//        tokenSprites.clear();
//
//        List<HexagonSprite> lod0Sprites = lodToSprites.get(0);
//        if (lod0Sprites == null) return;
//
//        for (HexagonSprite sprite : lod0Sprites) {
//            ITile tile = sprite.getTile();
//            float cx = sprite.getX() + sprite.getOriginX();
//            float cy = sprite.getY() + sprite.getOriginY();
//            List<NumberTokenSprite> tokens = tokenFactory.createTokens(tile.getProductionNumbers(), cx, cy, font);
//            tokenSprites.addAll(tokens);
//        }
    }

    private void regenerateHexagonSpritesForLod(TileTextureProvider textures, int lod) {
        List<PolygonTileSprite> sprites = new ArrayList<>(board.getTiles().size());
        Optional<ITileGrid> optGrid = board.getGrid();
        for (Map.Entry<ITileType, List<ITile>> e : board.getTilesGroupedByTileType().entrySet()) {
            ITileType type = e.getKey();
            TextureRegion region = textures.getTileTexture(lod, type.getId());
            for (ITile tile : e.getValue()) {
                ITileGrid grid = optGrid.orElseGet(() -> board.getGridForTile(tile));
                PolygonShape shape = grid.getPolygonShape(tile);
                PolygonTileSprite sprite = spriteFactory.create(tile, region, shape);
                sprites.add(sprite);
            }
        }
        lodToSprites.put(lod, sprites);
        if (lod == currentLod) {
            currentSpriteList = lodToSprites.get(currentLod);
        }
    }

    public void render(OrthographicCamera camera) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        polyBatch.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);

        int selectedLod = lodFromZoom(camera.zoom);
        if (selectedLod != currentLod) {
            currentLod = selectedLod;
            currentSpriteList = lodToSprites.getOrDefault(currentLod, Collections.emptyList());
        }

        polyBatch.begin();
        for (PolygonTileSprite s : currentSpriteList) s.draw(polyBatch);
        polyBatch.end();

        spriteBatch.begin();
        for (NumberTokenSprite t : tokenSprites) t.draw(spriteBatch);
        spriteBatch.end();
    }

    private int lodFromZoom(float zoom) {
        float hexWorldHeight = 2f * config.tileRadius();
        float screenScale = Gdx.graphics.getDensity() / zoom;
        float tilePixelHeight = screenScale * hexWorldHeight;

        if (tilePixelHeight > 256) return 0;
        else if (tilePixelHeight > 128) return 1;
        else if (tilePixelHeight > 64)  return 2;
        else return 3;
    }

    public void dispose() {
        polyBatch.dispose();
        spriteBatch.dispose();
    }
}
