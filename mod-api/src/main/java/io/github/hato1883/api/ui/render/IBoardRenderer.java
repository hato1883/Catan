package io.github.hato1883.api.ui.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.hato1883.api.assets.AssetUpgradeNotifier;
import io.github.hato1883.api.assets.IAssetProvider;
import io.github.hato1883.api.ui.model.IBoardView;

/**
 * Pluggable board renderer interface for mods and core.
 * Mods should use UIEvents for overlays, particles, etc. Only override this for exotic board rendering.
 */
public interface IBoardRenderer {
    void initializeSprites(IAssetProvider assets, IBoardView board);
    void render(OrthographicCamera camera, IBoardView board, IAssetProvider assets);
    void dispose();

    /**
     * Register for asset upgrade notifications. Default implementation does nothing.
     * Override if your renderer supports dynamic asset upgrades.
     */
    default void registerAssetUpgradeNotifier(AssetUpgradeNotifier notifier) {}
}
