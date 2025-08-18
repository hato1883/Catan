package io.github.hato1883.core.modloading.assets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.hato1883.api.mod.load.asset.TextureEntry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record AtlasCacheInfo(
    Map<String, String> modVersions,
    List<String> textures,
    Map<String, String> textureHashes,
    String category,
    int lod
) {

    public static AtlasCacheInfo load(Path jsonPath) throws IOException {
        if (Files.exists(jsonPath)) {
            String content = Files.readString(jsonPath);
            return new Gson().fromJson(content, AtlasCacheInfo.class);
        }
        return null;
    }

    public static void save(Path jsonPath, Map<String, String> modVersions,
                            List<TextureEntry> entries, String category, int lod) throws IOException {
        List<String> texIds = entries.stream()
            .map(e -> e.getId().toString())
            .toList();

        Map<String, String> hashes = entries.stream()
            .collect(Collectors.toMap(
                e -> e.getId().toString(),
                AtlasCacheInfo::computeHash // your method to hash file contents
            ));

        AtlasCacheInfo info = new AtlasCacheInfo(modVersions, texIds, hashes, category, lod);
        Files.writeString(jsonPath, new GsonBuilder().setPrettyPrinting().create().toJson(info));
    }

    /** Check if cache matches current mods and textures */
    public boolean matches(Map<String, String> currentMods, List<TextureEntry> currentTextures) {
        if (!modVersions.equals(currentMods)) return false;

        for (TextureEntry entry : currentTextures) {
            String currentHash = computeHash(entry);
            String cachedHash = textureHashes.get(entry.getId().toString());
            if (!Objects.equals(currentHash, cachedHash)) return false;
        }

        return true;
    }

    private static String computeHash(TextureEntry entry) {
        try {
            byte[] bytes;
            try (InputStream is = entry.openStream()) {
                bytes = is.readAllBytes();
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(bytes);
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash texture: " + entry.getId(), e);
        }
    }
}


