package io.github.hato1883.api.assets;

public interface TextureUpgradeNotifier extends AssetUpgradeNotifier {
    void onTextureUpgrade(TextureUpgradeCallback<?> callback);
}
