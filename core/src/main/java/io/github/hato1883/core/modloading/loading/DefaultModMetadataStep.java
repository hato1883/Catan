package io.github.hato1883.core.modloading.loading;

import io.github.hato1883.api.mod.load.IModMetadataReader;
import io.github.hato1883.api.mod.load.ModMetadata;
import org.slf4j.Logger;
import io.github.hato1883.api.LogManager;
import java.nio.file.Path;
import java.util.*;

/**
 * Step 2: Read metadata for each discovered mod and store in the context.
 */
public class DefaultModMetadataStep implements ModLoadingStep {
    private static final Logger LOGGER = LogManager.getLogger("ModLoading");
    private final IModMetadataReader metadataReader;

    public DefaultModMetadataStep(IModMetadataReader metadataReader) {
        this.metadataReader = metadataReader;
    }

    @Override
    public void execute(ModLoadingContext context) {
        List<Path> modPaths = context.getDiscoveredModPaths();
        Map<String, ModMetadata> highestVersionMeta = new HashMap<>();
        Map<String, Path> highestVersionPath = new HashMap<>();
        for (Path p : modPaths) {
            try {
                ModMetadata meta = metadataReader.readMetadata(p);
                String modid = meta.id();
                if (!highestVersionMeta.containsKey(modid)) {
                    highestVersionMeta.put(modid, meta);
                    highestVersionPath.put(modid, p);
                } else {
                    ModMetadata existing = highestVersionMeta.get(modid);
                    int cmp = compareSemanticVersion(meta.version(), existing.version());
                    if (cmp > 0) {
                        LOGGER.error("Duplicate modid '{}' found: version {} at {} is newer than version {} at {}. Keeping newer version.",
                            modid, meta.version(), p.getFileName(), existing.version(), highestVersionPath.get(modid).getFileName());
                        highestVersionMeta.put(modid, meta);
                        highestVersionPath.put(modid, p);
                    } else {
                        LOGGER.error("Duplicate modid '{}' found: version {} at {} is inferior to version {} at {}. Skipping inferior version.",
                            modid, meta.version(), p.getFileName(), existing.version(), highestVersionPath.get(modid).getFileName());
                    }
                }
                LOGGER.info("Found mod {} v{}", meta.id(), meta.version());
            } catch (Exception e) {
                LOGGER.error("Failed to read metadata from {}: {}", p.getFileName(), e.getMessage(), e);
            }
        }
        Map<ModMetadata, Path> metadataMap = new HashMap<>();
        for (String modid : highestVersionMeta.keySet()) {
            metadataMap.put(highestVersionMeta.get(modid), highestVersionPath.get(modid));
        }
        context.setModMetadataMap(metadataMap);
    }

    private int compareSemanticVersion(String v1, String v2) {
        String[] a1 = v1.split("[.-]", 4);
        String[] a2 = v2.split("[.-]", 4);
        for (int i = 0; i < 3; i++) {
            int n1 = i < a1.length ? parseIntSafe(a1[i]) : 0;
            int n2 = i < a2.length ? parseIntSafe(a2[i]) : 0;
            if (n1 != n2) return Integer.compare(n1, n2);
        }
        if (a1.length == a2.length && a1.length > 3) {
            return a1[3].compareToIgnoreCase(a2[3]);
        } else if (a1.length > 3) {
            return -1;
        } else if (a2.length > 3) {
            return 1;
        }
        return 0;
    }
    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }
}

