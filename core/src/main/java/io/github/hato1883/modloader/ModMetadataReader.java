package io.github.hato1883.modloader;

import com.google.gson.*;
import io.github.hato1883.modloader.dependency.LoadPriority;
import io.github.hato1883.modloader.dependency.ModDependency;
import io.github.hato1883.modloader.dependency.VersionConstraint;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Reads catan.mod.json into ModMetadata using Gson.
 */
public final class ModMetadataReader {

    private static final String METADATA_FILE = "catan.mod.json";

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(VersionConstraint.class, new VersionConstraintDeserializer())
        .registerTypeAdapter(ModMetadata.class, new ModMetadataDeserializer())
        .create();

    private ModMetadataReader() {}

    public static ModMetadata read(Path jarPath) throws IOException {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            ZipEntry entry = jarFile.getEntry(METADATA_FILE);
            if (entry == null) {
                throw new IOException("Missing " + METADATA_FILE + " in mod jar: " + jarPath);
            }
            try (InputStream in = jarFile.getInputStream(entry)) {
                return GSON.fromJson(new java.io.InputStreamReader(in), ModMetadata.class);
            }
        }
    }

    /**
     * Custom deserializer for VersionConstraint to validate and parse version strings.
     */
    private static class VersionConstraintDeserializer implements JsonDeserializer<VersionConstraint> {
        @Override
        public VersionConstraint deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String versionStr = json.getAsString();
            return VersionConstraint.parse(versionStr); // You implement this parse method!
        }
    }

    /**
     * Custom deserializer for ModMetadata that enforces mandatory fields and defaults.
     */
    private static class ModMetadataDeserializer implements JsonDeserializer<ModMetadata> {

        @Override
        public ModMetadata deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();

            // Required fields
            String id = getRequiredString(obj, "id");
            String name = getRequiredString(obj, "name");
            String version = getRequiredString(obj, "version");
            String entrypoint = getRequiredString(obj, "entrypoint");

            // Optional fields with defaults
            List<ModDependency> dependencies = Collections.emptyList();
            if (obj.has("dependencies") && obj.get("dependencies").isJsonArray()) {
                dependencies = context.deserialize(obj.get("dependencies"),
                    new com.google.gson.reflect.TypeToken<List<ModDependency>>() {
                    }.getType());
            }

            LoadPriority loadPriority = LoadPriority.NORMAL; // default
            if (obj.has("loadPriority") && !obj.get("loadPriority").isJsonNull()) {
                try {
                    loadPriority = LoadPriority.valueOf(obj.get("loadPriority").getAsString().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException("Invalid loadPriority value: " + obj.get("loadPriority").getAsString());
                }
            }

            // Return fully built metadata
            return new ModMetadata(id, name, version, entrypoint, dependencies, loadPriority);
        }

        private static String getRequiredString(JsonObject obj, String key) {
            if (!obj.has(key) || obj.get(key).isJsonNull() || obj.get(key).getAsString().isBlank()) {
                throw new JsonParseException("Missing or empty required field: " + key);
            }
            return obj.get(key).getAsString();
        }
    }
}
