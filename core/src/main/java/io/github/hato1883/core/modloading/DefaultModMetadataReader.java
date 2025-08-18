package io.github.hato1883.core.modloading;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.hato1883.api.mod.load.*;
import io.github.hato1883.api.mod.load.ModMetadata;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * GSON-based implementation of mod metadata reader.
 * This class handles the specific JSON format and deserialization logic.
 */
public class DefaultModMetadataReader implements IModMetadataReader {

    private static final String METADATA_FILE = "catan.mod.json";
    private static final Gson GSON = createGson();

    @Override
    public ModMetadata readMetadata(Path modPath) throws IOException {
        try (InputStream inputStream = openMetadataStream(modPath)) {
            return parseMetadata(inputStream);
        } catch (JsonParseException e) {
            throw new ModMetadataParseException("Failed to parse metadata from " + modPath, e);
        }
    }

    private InputStream openMetadataStream(Path modPath) throws IOException {
        if (Files.isDirectory(modPath)) {
            return openFromDirectory(modPath);
        } else {
            return openFromJar(modPath);
        }
    }

    private InputStream openFromDirectory(Path modPath) throws IOException {
        Path metadataPath = modPath.resolve(METADATA_FILE);
        if (!Files.exists(metadataPath)) {
            throw new FileNotFoundException("Metadata file not found: " + metadataPath);
        }
        return Files.newInputStream(metadataPath);
    }

    private InputStream openFromJar(Path modPath) throws IOException {
        JarFile jarFile = new JarFile(modPath.toFile());
        ZipEntry entry = findMetadataEntry(jarFile);
        if (entry == null) {
            jarFile.close();
            throw new FileNotFoundException("Metadata file not found in jar: " + modPath);
        }
        return jarFile.getInputStream(entry);
    }

    private ZipEntry findMetadataEntry(JarFile jarFile) {
        ZipEntry entry = jarFile.getEntry(METADATA_FILE);
        if (entry == null) {
            entry = jarFile.getEntry("META-INF/" + METADATA_FILE);
        }
        return entry;
    }

    private ModMetadata parseMetadata(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return GSON.fromJson(reader, ModMetadata.class);
        }
    }

    private static Gson createGson() {
        return new GsonBuilder()
            .registerTypeAdapter(ModMetadata.class, new ModMetadataDeserializer())
            .registerTypeAdapter(ModDependency.class, new ModDependencyDeserializer())
            .registerTypeAdapter(LoadPriority.class, new LoadPriorityDeserializer())
            .create();
    }

    /**
     * Deserializer for ModMetadata using the builder pattern
     */
    private static class ModMetadataDeserializer implements JsonDeserializer<ModMetadata> {
        @Override
        public ModMetadata deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject obj = json.getAsJsonObject();

            try {
                ModMetadata.Builder builder = ModMetadata.builder()
                    .id(getRequired(obj, "id"))
                    .entrypoint(getRequired(obj, "entrypoint"))
                    .version(getRequired(obj, "version"));

                // Optional fields
                if (obj.has("name")) {
                    builder.name(obj.get("name").getAsString());
                }

                if (obj.has("description")) {
                    builder.description(obj.get("description").getAsString());
                }

                if (obj.has("dependencies") && obj.get("dependencies").isJsonArray()) {
                    Type listType = new TypeToken<List<ModDependency>>() {}.getType();
                    List<ModDependency> dependencies = context.deserialize(obj.get("dependencies"), listType);
                    builder.dependencies(dependencies);
                }

                if (obj.has("loadPriority") && !obj.get("loadPriority").isJsonNull()) {
                    LoadPriority priority = context.deserialize(obj.get("loadPriority"), LoadPriority.class);
                    builder.loadPriority(priority);
                }

                return builder.build();

            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Invalid mod metadata: " + e.getMessage(), e);
            }
        }

        private String getRequired(JsonObject obj, String key) {
            if (!obj.has(key) || obj.get(key).isJsonNull()) {
                throw new JsonParseException("Missing required field: " + key);
            }
            String value = obj.get(key).getAsString();
            if (value.isBlank()) {
                throw new JsonParseException("Required field cannot be blank: " + key);
            }
            return value;
        }
    }

    /**
     * Deserializer for ModDependency
     */
    private static class ModDependencyDeserializer implements JsonDeserializer<ModDependency> {
        @Override
        public ModDependency deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject obj = json.getAsJsonObject();

            String modId = getRequired(obj, "id");
            String version = obj.has("version") ? obj.get("version").getAsString() : "*";
            boolean optional = obj.has("optional") && obj.get("optional").getAsBoolean();

            return new ModDependency(modId, VersionConstraint.parse(version), optional);
        }

        private String getRequired(JsonObject obj, String key) {
            if (!obj.has(key) || obj.get(key).isJsonNull()) {
                throw new JsonParseException("Missing required field: " + key);
            }
            String value = obj.get(key).getAsString();
            if (value.isBlank()) {
                throw new JsonParseException("Required field cannot be blank: " + key);
            }
            return value;
        }
    }

    /**
     * Deserializer for LoadPriority enum with case-insensitive parsing
     */
    private static class LoadPriorityDeserializer implements JsonDeserializer<LoadPriority> {
        @Override
        public LoadPriority deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            String priorityStr = json.getAsString().toUpperCase();
            try {
                return LoadPriority.valueOf(priorityStr);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Invalid loadPriority value: " + json.getAsString() +
                    ". Valid values are: " + java.util.Arrays.toString(LoadPriority.values()));
            }
        }
    }
}
