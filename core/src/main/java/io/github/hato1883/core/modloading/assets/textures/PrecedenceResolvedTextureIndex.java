package io.github.hato1883.core.modloading.assets.textures;

import io.github.hato1883.api.mod.load.asset.TextureEntry;
import io.github.hato1883.api.Identifier;
import java.util.*;

/**
 * Builds a precedence-resolved index of texture assets from multiple sources.
 * The first source in the list has the highest precedence.
 */
public class PrecedenceResolvedTextureIndex {
    private final Map<Identifier, TextureEntry> resolved;

    /**
     * @param sources List of lists of TextureEntry, ordered by precedence (highest first)
     */
    public PrecedenceResolvedTextureIndex(List<List<TextureEntry>> sources) {
        this.resolved = new LinkedHashMap<>();
        for (List<TextureEntry> source : sources) {
            for (TextureEntry entry : source) {
                // Only add if not already present (higher precedence wins)
                this.resolved.putIfAbsent(entry.getId(), entry);
            }
        }
    }

    public Collection<TextureEntry> getResolvedEntries() {
        return resolved.values();
    }

    public TextureEntry get(Identifier id) {
        return resolved.get(id);
    }
}

