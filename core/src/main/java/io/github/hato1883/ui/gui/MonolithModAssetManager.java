package io.github.hato1883.ui.gui;
// ModAssetManager.java
// Requires LibGDX: com.badlogic.gdx.*
// Minor utility classes ModInfo and ModSource are included below.

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.Stream;

import static com.badlogic.gdx.graphics.GL20.GL_MAX_TEXTURE_SIZE;

public class MonolithModAssetManager {

    // Configuration
    public static class Config {

        // Array of different lod resolutions
        public int[] lodSizes = new int[]{64, 128, 256, 512}; // default LODs

        // Set Atlas size to the smallest of 4096 OR GL_MAX_TEXTURE_SIZE
        // This will make it so any GPU capable of larger atlas still only get 4096 (Huge)
        // But mobile devices or lower end hardware which are weaker can get smaller atlas
        public int maxAtlasSize = Math.min(GL_MAX_TEXTURE_SIZE, 4096);

        // Stop Texture bleeds
        public int padding = 4;

        // Use MipMap for pseudo LOD
        public boolean useMipMaps = true;

        // Fix texture scaling to use MipMap when zooming out and Linear when zooming in
        public TextureFilter minFilter = TextureFilter.MipMapLinearLinear;
        public TextureFilter magFilter = TextureFilter.Linear;

        // categories to consider packing (if null, autodiscover)
        public Set<String> categories = null;
    }

    private final Config cfg;
    private final Map<String, ModSource> loadedMods = new LinkedHashMap<>();

    // Map: category -> lodSize -> TextureAtlas
    private final Map<String, Map<Integer, TextureAtlas>> atlases = new HashMap<>();

    // Map: regionKey -> TextureRegion (regionKey = modId + ":" + category + "/" + name + "@" + lod)
    private final Map<String, TextureRegion> regionIndex = new HashMap<>();

    public MonolithModAssetManager(Config config) {
        this.cfg = config;
    }

    // Call this once for every mod that's loaded (either jar path or exploded dir)
    public void registerMod(ModInfo modInfo, Path modPath) throws IOException {
        ModSource src = new ModSource(modInfo, modPath);
        src.scan(); // find assets and prebuilt atlases
        loadedMods.put(modInfo.modId, src);
    }

    // Build runtime atlases for all categories and LODs
    public void buildAtlases() throws IOException {
        // 1) determine categories
        Set<String> categories = new HashSet<>();
        for (ModSource src : loadedMods.values()) {
            categories.addAll(src.getCategories());
        }
        if (cfg.categories != null && !cfg.categories.isEmpty()) {
            categories.retainAll(cfg.categories);
        }

        // 2) per category and LOD, either use prebuilt atlas(s) or pack discovered textures
        for (String category : categories) {
            Map<Integer, TextureAtlas> lodAtlasMap = new HashMap<>();
            for (int lod : cfg.lodSizes) {
                // step: collect textures for category+lod
                List<ModSource.TextureEntry> entries = new ArrayList<>();
                for (ModSource src : loadedMods.values()) {
                    // if prebuilt atlas for this mod & category & lod exists, we'll prefer it later
                    entries.addAll(src.getTexturesFor(category, lod));
                }

                // If any mod provided a prebuilt atlas for this category/lod, prefer merging or using it.
                // Simpler approach: if exactly one prebuilt atlas exists and no other textures, use it.
                // If multiple prebuilt atlases exist, we'll load them separately and copy regions into the index.
                List<ModSource.PrebuiltAtlas> prebuilt = new ArrayList<>();
                for (ModSource src : loadedMods.values()) {
                    src.getPrebuiltAtlases(category, lod).ifPresent(prebuilt::add);
                }

                if (!prebuilt.isEmpty() && entries.isEmpty()) {
                    // load all prebuilt atlases (could be multiple mods)
                    TextureAtlas combined = new TextureAtlas();
                    for (ModSource.PrebuiltAtlas pa : prebuilt) {
                        TextureAtlas t = pa.loadAtlas();
                        // copy regions into combined (TextureAtlas doesn't provide merge API) - but we can map regions directly
                        for (TextureAtlas.AtlasRegion r : new Array.ArrayIterator<>(t.getRegions())) {
                            String key = buildRegionKey(pa.modId, category, r.name, lod);
                            regionIndex.put(key, r);
                        }
                        lodAtlasMap.put(lod, t); // last one wins for getAtlas(category, lod)
                    }
                    // continue to next lod
                    continue;
                }

                if (entries.isEmpty()) {
                    // nothing to pack for this category/lod
                    continue;
                }

                // 3) pack textures into PixmapPacker
                PixmapPacker packer = new PixmapPacker(cfg.maxAtlasSize, cfg.maxAtlasSize, Pixmap.Format.RGBA8888, cfg.padding, false);

                for (ModSource.TextureEntry te : entries) {
                    try (InputStream is = te.open()) {
                        Pixmap pix = new Pixmap(ls); // LibGDX Pixmap(InputStream) exists if using FileHandle#read
                        // Name regions as modId + "/" + originalName to avoid collisions
                        String regionName = te.modId + "/" + te.name;
                        packer.pack(regionName, pix);
                        pix.dispose();
                    } catch (Exception e) {
                        System.err.println("Failed to pack texture " + te + ": " + e.getMessage());
                    }
                }

                // 4) generate TextureAtlas
                TextureAtlas atlas = packer.generateTextureAtlas(cfg.minFilter, cfg.magFilter, cfg.useMipMaps);
                lodAtlasMap.put(lod, atlas);

                // 5) index regions
                for (TextureAtlas.AtlasRegion region : new Array.ArrayIterator<>(atlas.getRegions())) {
                    // region.name is regionName we gave above (modId/name)
                    String[] parts = region.name.split("/", 2);
                    String modId = parts.length > 1 ? parts[0] : "unknown";
                    String name = parts.length > 1 ? parts[1] : parts[0];
                    String key = buildRegionKey(modId, category, name, lod);
                    regionIndex.put(key, region);
                }
            } // end lod loop

            atlases.put(category, lodAtlasMap);
        } // end category loop
    }

    // Lookup API
    public TextureRegion getRegion(String modId, String category, String name, int lod) {
        String key = buildRegionKey(modId, category, name, lod);
        TextureRegion r = regionIndex.get(key);
        if (r != null) return r;

        // fallback: find nearest lod (higher resolution)
        int fallback = findNearestLODWithRegion(modId, category, name, lod);
        if (fallback != -1) {
            return regionIndex.get(buildRegionKey(modId, category, name, fallback));
        }
        return null;
    }

    public TextureAtlas getAtlas(String category, int lod) {
        Map<Integer, TextureAtlas> m = atlases.get(category);
        if (m == null) return null;
        return m.get(lod);
    }

    // Utility
    private int findNearestLODWithRegion(String modId, String category, String name, int requested) {
        // choose nearest by size difference
        int best = -1;
        int bestDiff = Integer.MAX_VALUE;
        for (int lod : cfg.lodSizes) {
            String k = buildRegionKey(modId, category, name, lod);
            if (regionIndex.containsKey(k)) {
                int diff = Math.abs(lod - requested);
                if (diff < bestDiff) {
                    bestDiff = diff;
                    best = lod;
                }
            }
        }
        return best;
    }

    private static String buildRegionKey(String modId, String category, String name, int lod) {
        return modId + ":" + category + "/" + name + "@" + lod;
    }

    // ========== Inner helper classes: ModInfo + ModSource ==========
    public record ModInfo(String modId, String entryPoint) {}

    // ModSource represents either a jar or exploded directory
    private static class ModSource {
        public final ModInfo info;
        public final Path path; // jar file or directory

        // Found assets
        // Map: category -> lod -> list of entries
        private final Map<String, Map<Integer, List<TextureEntry>>> textures = new HashMap<>();
        private final Map<String, Map<Integer, PrebuiltAtlas>> prebuiltAtlases = new HashMap<>();

        public ModSource(ModInfo info, Path path) {
            this.info = info;
            this.path = path;
        }

        public void scan() throws IOException {
            if (Files.isDirectory(path)) {
                scanExploded();
            } else {
                scanJar();
            }
        }

        private void scanExploded() throws IOException {
            // Walk assets/<modid>/textures/...
            Path assetsRoot = path.resolve("assets").resolve(info.modId).resolve("textures");
            if (!Files.exists(assetsRoot)) return;

            try (Stream<Path> stream = Files.walk(assetsRoot)) {
                stream.forEach(p -> {
                    if (Files.isRegularFile(p)) {
                        String rel = assetsRoot.relativize(p).toString().replace('\\', '/');
                        handleTextureFile(rel, p.toUri().toString());
                    }
                });
            }

            // scan atlases
            Path atlases = path.resolve("assets").resolve(info.modId).resolve("atlases");
            if (Files.exists(atlases)) {

                try (Stream<Path> stream = Files.walk(atlases)) {
                    stream.forEach(p -> {
                        if (Files.isRegularFile(p) && p.toString().endsWith(".atlas")) {
                            // assume same-name .png exists next to it
                            Path png = p.getParent().resolve(p.getFileName().toString().replaceFirst("\\.atlas$", ".png"));
                            if (Files.exists(png)) {
                                // naive: extract category/lod from filename: category.lod64.atlas or category.atlas
                                String fname = p.getFileName().toString();
                                // e.g. tiles.lod256.atlas or tiles.atlas
                                String without = fname.replaceFirst("\\.atlas$", "");
                                int lod = extractLodFromName(without);
                                String category = without.replaceAll("\\.lod\\d+$", "");
                                registerPrebuiltAtlas(category, lod, new PrebuiltAtlas(info.modId, p.toAbsolutePath().toString(), png.toAbsolutePath().toString()));
                            }
                        }
                    });
                }
            }
        }

        private void scanJar() throws IOException {
            try (JarFile jf = new JarFile(path.toFile())) {
                Enumeration<JarEntry> en = jf.entries();
                String prefix = "assets/" + info.modId + "/textures/";
                String atlasPrefix = "assets/" + info.modId + "/atlases/";
                while (en.hasMoreElements()) {
                    JarEntry e = en.nextElement();
                    String name = e.getName();
                    if (e.isDirectory()) continue;
                    if (name.startsWith(prefix)) {
                        String rel = name.substring(prefix.length()); // e.g. tiles/lod256/castle.png or tiles/castle.png
                        handleTextureFile(rel, "jar:" + path.toUri() + "!/" + name);
                    } else if (name.startsWith(atlasPrefix) && name.endsWith(".atlas")) {
                        // find corresponding png entry
                        String atlasRel = name.substring(atlasPrefix.length()); // e.g. tiles.lod256.atlas
                        String pngName = atlasRel.replaceFirst("\\.atlas$", ".png");
                        JarEntry pngEntry = jf.getJarEntry(atlasPrefix + pngName);
                        if (pngEntry != null) {
                            int lod = extractLodFromName(atlasRel.replaceFirst("\\.atlas$", ""));
                            String category = atlasRel.replaceFirst("\\.lod\\d+\\.atlas$", "").replaceFirst("\\.atlas$", "");
                            // Note: to load Jar resources later we open stream via JarFile if needed
                            registerPrebuiltAtlas(category, lod, new PrebuiltAtlas(info.modId, path.toAbsolutePath().toString() + "::" + name, path.toAbsolutePath().toString() + "::" + atlasPrefix + pngName));
                        }
                    }
                }
            }
        }

        private void handleTextureFile(String relPath, String resourceRef) {
            // relPath e.g. "tiles/lod256/castle.png" or "tiles/castle@2x.png"
            String[] parts = relPath.split("/", 2);
            if (parts.length == 0) return;
            String category = parts[0];
            String remainder = parts.length > 1 ? parts[1] : "";

            // determine lod: check folder like lod256 or file name suffix like @2x or @256
            int lod = detectLOD(remainder);

            // extract filename base
            String name = remainder;
            if (name.contains("/")) name = name.substring(name.lastIndexOf('/') + 1);
            name = name.replaceAll("\\.(png|jpg|jpeg)$", "");
            name = name.replaceAll("@\\dx$", ""); // remove @2x etc.

            textures.computeIfAbsent(category, k -> new HashMap<>())
                .computeIfAbsent(lod, k -> new ArrayList<>())
                .add(new TextureEntry(info.modId, category, name, resourceRef));
        }

        private static int detectLOD(String remainder) {
            // remainder may start with "lod256/..." or file name may have @2x or @256
            if (remainder.startsWith("lod")) {
                // lodXXX/...
                int slash = remainder.indexOf('/');
                String first = slash == -1 ? remainder : remainder.substring(0, slash);
                try {
                    return Integer.parseInt(first.substring(3));
                } catch (Exception ignored) { }
            }
            // check suffix @2x or @256
            String fname = remainder;
            if (fname.contains("/")) fname = fname.substring(fname.lastIndexOf('/') + 1);
            // foo@2x.png
            int at = fname.lastIndexOf('@');
            if (at != -1) {
                String s = fname.substring(at + 1).replaceAll("\\.(png|jpg|jpeg)$", "");
                if (s.endsWith("x")) s = s.substring(0, s.length()-1);
                try { return Integer.parseInt(s); } catch (Exception ignored) {}
            }
            // default to smallest LOD (0 indicates unspecified; caller will interpret)
            return 0;
        }

        private static int extractLodFromName(String name) {
            // e.g. "tiles.lod256" -> 256 ; "tiles" -> 0
            int idx = name.indexOf(".lod");
            if (idx != -1) {
                try {
                    return Integer.parseInt(name.substring(idx + 4));
                } catch (Exception ignored) {}
            }
            return 0;
        }

        public Set<String> getCategories() {
            return textures.keySet();
        }

        public List<TextureEntry> getTexturesFor(String category, int lod) {
            Map<Integer, List<TextureEntry>> m = textures.get(category);
            if (m == null) return Collections.emptyList();
            List<TextureEntry> out = new ArrayList<>();
            // if exact lod exists, use it
            if (m.containsKey(lod) && lod != 0) {
                out.addAll(m.get(lod));
            } else {
                // fallback: include entries with lod==0 (unspecified) and all entries for other lods (they'll be packed into requested atlas).
                // We can be smarter (scale) but keep it simple: include all non-empty lists.
                for (List<TextureEntry> lst : m.values()) out.addAll(lst);
            }
            return out;
        }

        public Optional<PrebuiltAtlas> getPrebuiltAtlases(String category, int lod) {
            Map<Integer, PrebuiltAtlas> m = prebuiltAtlases.get(category);
            if (m == null) return Optional.empty();
            return Optional.ofNullable(m.get(lod));
        }

        private void registerPrebuiltAtlas(String category, int lod, PrebuiltAtlas pa) {
            prebuiltAtlases.computeIfAbsent(category, k -> new HashMap<>()).put(lod, pa);
        }

        // small value objects
        public static class TextureEntry {
            public final String modId;
            public final String category;
            public final String name;
            public final String resourceRef; // jar:path or file uri

            public TextureEntry(String modId, String category, String name, String resourceRef) {
                this.modId = modId;
                this.category = category;
                this.name = name;
                this.resourceRef = resourceRef;
            }

            public InputStream open() throws IOException {
                if (resourceRef.startsWith("jar:") || resourceRef.contains("::")) {
                    // resourceRef for jar-based scanning may be "jar:file:///...!/<entry>"
                    // We will attempt to split custom encoded resourceRef and open via JarFile if necessary.
                    // Simple: look for pattern jar:fileURI!/entry
                    if (resourceRef.startsWith("jar:")) {
                        // example: "jar:file:/path/to/mod.jar!/assets/modid/textures/..."
                        int bang = resourceRef.indexOf("!/");
                        if (bang > 0) {
                            String jarUri = resourceRef.substring("jar:".length(), bang); // file:/...
                            String entry = resourceRef.substring(bang + 2);
                            try {
                                Path jarPath = Paths.get(new URI(jarUri));
                                try (JarFile jf = new JarFile(jarPath.toFile())) {
                                    JarEntry je = jf.getJarEntry(entry);
                                    if (je == null) throw new FileNotFoundException("Entry not found in jar: " + entry);
                                    return jf.getInputStream(je);
                                }
                            } catch (URISyntaxException e) {
                                throw new IOException(e);
                            }
                        }
                    } else if (resourceRef.contains("::")) {
                        // our custom join for stored prebuilt atlas references (path::entry)
                        String[] parts = resourceRef.split("::", 2);
                        String jarPath = parts[0];
                        String entry = parts[1];
                        try (JarFile jf = new JarFile(new File(jarPath))) {
                            JarEntry je = jf.getJarEntry(entry);
                            if (je == null) throw new FileNotFoundException("Entry not found in jar: " + entry);
                            return jf.getInputStream(je);
                        }
                    }
                }
                // Otherwise assume file URI
                if (resourceRef.startsWith("file:")) {
                    try {
                        Path p = Paths.get(new URI(resourceRef));
                        return Files.newInputStream(p);
                    } catch (URISyntaxException e) {
                        return Files.newInputStream(Paths.get(resourceRef.replaceFirst("^file:", "")));
                    }
                }
                // last-ditch: treat as plain path
                return new FileInputStream(resourceRef);
            }

            @Override
            public String toString() {
                return modId + "/" + category + "/" + name + " (" + resourceRef + ")";
            }
        }

        public static class PrebuiltAtlas {
            public final String modId;
            public final String atlasRef; // jarRef::entry or absolute png path; simplified representation
            public final String pngRef;

            public PrebuiltAtlas(String modId, String atlasRef, String pngRef) {
                this.modId = modId;
                this.atlasRef = atlasRef;
                this.pngRef = pngRef;
            }

            public TextureAtlas loadAtlas() {
                // NOTE: loading TextureAtlas from arbitrary jar paths is non-trivial with LibGDX default API.
                // For simplicity, if the atlasRef is a regular file path, load direct; else throw
                try {
                    if (atlasRef.contains("::")) {
                        // not implemented: you would need to extract atlas + png to temp files and load via TextureAtlas(FileHandle)
                        throw new UnsupportedOperationException("Loading prebuilt atlas from jar not implemented; please ship exploded assets or provide atlas as files.");
                    } else {
                        FileHandle atlasHandle = Gdx.files.absolute(atlasRef);
                        return new TextureAtlas(atlasHandle);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load prebuilt atlas: " + atlasRef, e);
                }
            }
        }
    }
}

