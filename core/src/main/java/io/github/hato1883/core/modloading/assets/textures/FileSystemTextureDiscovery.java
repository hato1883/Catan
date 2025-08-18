package io.github.hato1883.core.modloading.assets.textures;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.mod.load.asset.AssetCategory;
import io.github.hato1883.api.mod.load.asset.TextureEntry;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileSystemTextureDiscovery {

    public Map<AssetCategory, Map<Integer, List<TextureEntry>>> discoverForMod(
        String modId,
        FileSystem fs, // Filesystem within a jar or folder
        Path modArchivePath // Path to either a jar or folder
    ) throws IOException {
        Map<AssetCategory, Map<Integer, List<TextureEntry>>> result = new HashMap<>();

        Path root = fs.getPath("assets", modId, "textures");

        for (AssetCategory category : AssetCategory.values()) {
            Path categoryPath = root.resolve(category.getCategory().toLowerCase());
            if (!Files.exists(categoryPath)) continue;

            try (Stream<Path> walk = Files.walk(categoryPath)) {
                walk.filter(Files::isRegularFile)
                    .filter(this::isImageFile)
                    .forEach(file -> {
                        int lod = extractLod(file);
                        Path relative = categoryPath.relativize(file);
                        Identifier id = Identifier.of(
                            modId,
                            relative.getFileName().toString().split("\\.")[0]
                        );


                        String internalPath = "assets/" + modId + "/textures/" + category.getCategory().toLowerCase() + "/" + relative;

                        TextureEntry entry = new TextureEntry(
                            category,
                            lod,
                            id,
                            modArchivePath,
                            internalPath
                        );

                        result.computeIfAbsent(category, c -> new HashMap<>())
                            .computeIfAbsent(lod, l -> new ArrayList<>())
                            .add(entry);
                    }
                );
            }
        }
        return result;
    }



    private boolean isImageFile(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg");
    }

    private int extractLod(Path file) {
        // Example: "house_lod2.png" → 2
        String name = file.toString().toLowerCase();
        Matcher m = Pattern.compile("lod(\\d+)").matcher(name);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }
}

