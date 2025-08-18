package io.github.hato1883.api.mod.load.asset;

import io.github.hato1883.api.mod.load.ILoadedMod;

import java.io.IOException;
import java.util.List;

public interface IModAssetLoader {
    void loadAssets(List<ILoadedMod> mods) throws IOException;
}
