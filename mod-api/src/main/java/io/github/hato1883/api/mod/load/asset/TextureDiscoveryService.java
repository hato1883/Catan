package io.github.hato1883.api.mod.load.asset;

import io.github.hato1883.api.mod.load.ILoadedMod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TextureDiscoveryService {
    Map<ILoadedMod, Map<AssetCategory, Map<Integer, List<TextureEntry>>>> discover(List<ILoadedMod> mods) throws IOException;
}
