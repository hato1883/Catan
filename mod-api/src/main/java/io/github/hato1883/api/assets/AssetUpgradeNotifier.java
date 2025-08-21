package io.github.hato1883.api.assets;

public interface AssetUpgradeNotifier {
    <T> void onAssetUpgrade(Class<T> assetType, AssetUpgradeCallback<T> callback);
}
