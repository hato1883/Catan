package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.dependency.ModWithPath;
import io.github.hato1883.api.mod.load.asset.TextureEntry;
import io.github.hato1883.api.mod.CatanMod;
import io.github.hato1883.api.mod.load.ILoadedMod;
import java.nio.file.Path;
import java.util.*;

/**
 * Holds all state and results for the mod loading pipeline.
 */
public class ModLoadingContext {
    // Step 1: Discovery
    private List<Path> discoveredModPaths = new ArrayList<>();
    // Step 2: Metadata
    private Map<ModMetadata, Path> modMetadataMap = new HashMap<>();
    // Step 3: Dependency resolution
    private List<ModWithPath> orderedMods = new ArrayList<>();
    // Step 4: Loaded mod instances
    private List<ILoadedMod> loadedMods = new ArrayList<>();
    // Step 5: Asset info (textures, etc.)
    private List<List<TextureEntry>> allTextureSources = new ArrayList<>();
    // Add more fields as needed for other steps

    public List<Path> getDiscoveredModPaths() { return discoveredModPaths; }
    public void setDiscoveredModPaths(List<Path> paths) { this.discoveredModPaths = paths; }

    public Map<ModMetadata, Path> getModMetadataMap() { return modMetadataMap; }
    public void setModMetadataMap(Map<ModMetadata, Path> map) { this.modMetadataMap = map; }

    public List<ModWithPath> getOrderedMods() { return orderedMods; }
    public void setOrderedMods(List<ModWithPath> orderedMods) { this.orderedMods = orderedMods; }

    public List<ILoadedMod> getLoadedMods() { return loadedMods; }
    public void setLoadedMods(List<ILoadedMod> loadedMods) { this.loadedMods = loadedMods; }

    public List<List<TextureEntry>> getAllTextureSources() { return allTextureSources; }
    public void setAllTextureSources(List<List<TextureEntry>> sources) { this.allTextureSources = sources; }
}
