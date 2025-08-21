package io.github.hato1883.core.assets.management.textures;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.LogManager;
import io.github.hato1883.api.assets.AssetUpgradeCallback;
import io.github.hato1883.api.assets.TextureUpgradeCallback;
import io.github.hato1883.api.assets.TextureUpgradeNotifier;
import io.github.hato1883.api.mod.load.asset.AssetCategory;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class LodAwareTileTextureProvider implements TileTextureProvider, TextureUpgradeNotifier {

    private final boolean delayedMode = false; // toggle this to switch behaviors
    private final long delayMillis = 5000; // 5 seconds
    private long lastCallbackTime = 0;

    private static final Logger LOGGER = LogManager.getLogger("TileTextures");

    private final AssetManager assetManager;
    private final Path textureAssetRoot;

    // Safe to mutate while iterating during notify
    private final List<TextureUpgradeCallback<?>> upgradeCallbacks = new CopyOnWriteArrayList<>();

    // We only need one pending entry per LOD (atlas-level loading)
    private final Set<Integer> pendingLods = new HashSet<>();

    // Double-buffered notification queue
    private final List<Runnable> pendingNotifications = new ArrayList<>();
    private final List<Runnable> activeNotifications = new ArrayList<>();

    public LodAwareTileTextureProvider(AssetManager assetManager, Path textureAssetRoot) {
        this.assetManager = assetManager;
        this.textureAssetRoot = textureAssetRoot;
    }

    @Override
    public TextureRegion getTileTexture(int lod, Identifier tileTypeId) {
        String regionName = String.format("%s/%s", tileTypeId.getNamespace(), tileTypeId.getPath());

        // Try requested LOD → ... → LOD3
        for (int candidateLod = lod; candidateLod <= 3; candidateLod++) {
            Path path = atlasPath(AssetCategory.TILE, candidateLod);
            if (Files.exists(path) && assetManager.isLoaded(path.toString(), TextureAtlas.class)) {
                TextureAtlas atlas = assetManager.get(path.toString(), TextureAtlas.class);
                TextureRegion region = atlas.findRegion(regionName);
                if (region != null) {
                    if (candidateLod > lod) {
                        // We used a fallback; remember that the requested LOD should be upgraded later.
                        if (pendingLods.add(lod)) {
                            LOGGER.warn("Fallback LOD({}) → queued upgrade for requested LOD({})", candidateLod, lod);
                        }
                    }
                    return region;
                }
            }
        }

        // Last-resort: force-load LOD3 atlas (blocks until loaded) and return its region if present
        LOGGER.warn("No texture fallbacks for {} at LOD({}) — force loading LOD(3)", tileTypeId, lod);
        Path lod3path = atlasPath(AssetCategory.TILE, 3);
        assetManager.finishLoadingAsset(lod3path.toString());
        TextureAtlas atlas = assetManager.get(lod3path.toString(), TextureAtlas.class);
        TextureRegion region = atlas.findRegion(regionName);
        if (region == null) {
            LOGGER.error("Region '{}' not found in forced LOD(3) atlas! Returning null.", regionName);
        }
        return region;
    }

    /** Call this once per frame, AFTER assetManager.update(). */
    public void update() {
        long now = System.currentTimeMillis();

        // 1) Move pending LODs that are loaded into active notifications
        for (Iterator<Integer> it = pendingLods.iterator(); it.hasNext(); ) {
            int lod = it.next();
            Path path = atlasPath(AssetCategory.TILE, lod);
            if (Files.exists(path) && assetManager.isLoaded(path.toString(), TextureAtlas.class)) {
                LOGGER.info("LOD({}) atlas is now loaded — scheduling upgrade notification", lod);
                activeNotifications.add(() -> notifyUpgrade(/*id=*/null, lod));
                it.remove();
            }
        }

        // 2) Fire **one notification per update** if enough time has passed
        // 2) Execute notifications
        if (delayedMode) {
            // Only fire one per update if enough time has passed
            if (!activeNotifications.isEmpty() && now - lastCallbackTime >= delayMillis) {
                Runnable r = activeNotifications.removeFirst();
                if (r != null) {
                    r.run();
                    lastCallbackTime = now;
                }
            }
        } else {
            // Instant mode: fire all pending notifications in this update
            while (!activeNotifications.isEmpty()) {
                activeNotifications.removeFirst().run();
            }
        }
    }

    @Override
    public void onTextureUpgrade(TextureUpgradeCallback<?> callback) {
        // Only add if the callback is for TileTextureProvider
        upgradeCallbacks.add(callback);
    }

    @Override
    public <T> void onAssetUpgrade(Class<T> assetType, AssetUpgradeCallback<T> callback) {
        // Only handle texture upgrade notifiers
        if (assetType == TextureUpgradeNotifier.class) {
            // Safe cast: we know the callback is for textures
            @SuppressWarnings("unchecked")
            AssetUpgradeCallback<LodAwareTileTextureProvider> typed = (io.github.hato1883.api.assets.AssetUpgradeCallback<LodAwareTileTextureProvider>) callback;
            // Wrap as a TextureUpgradeCallback for compatibility
            this.onTextureUpgrade((provider, id, lod) -> typed.onUpgrade(this, id, lod));
        }
        // else: ignore other asset types
    }

    private void notifyUpgrade(Identifier idOrNull, int lod) {
        for (var cb : upgradeCallbacks) {
            @SuppressWarnings("unchecked")
            TextureUpgradeCallback<TileTextureProvider> typedCb = (TextureUpgradeCallback<TileTextureProvider>) cb;
            typedCb.onUpgrade(this, idOrNull, lod);
        }
    }

    public void onLodAtlasLoaded(int lod) {
        notifyUpgrade(null, lod);
    }

    private Path atlasPath(AssetCategory category, int lod) {
        return textureAssetRoot
            .resolve(category.getCategory())
            .resolve("lod" + lod)
            .resolve(String.format("combined_%s_lod%d.atlas", category.getCategory(), lod));
    }
}
