package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hato1883.api.mod.load.*;
import java.io.*;
import java.nio.file.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Jackson-based implementation of mod metadata reader with JSON5-like support.
 */
public class DefaultModMetadataReader implements IModMetadataReader {

    private static final String METADATA_FILE = "catan.mod.json5";
    private static final ObjectMapper JACKSON = createJackson();

    @Override
    public ModMetadata readMetadata(Path modPath) {
        try (InputStream inputStream = openMetadataStream(modPath)) {
            return parseMetadata(inputStream, modPath.toString());
        } catch (IllegalArgumentException e) {
            throw new ModMetadataParseException("Failed to parse metadata from " + modPath + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ModMetadataParseException("JSON5-like syntax error in " + modPath + ": " + e.getMessage(), e);
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

    private ModMetadata parseMetadata(InputStream inputStream, String source) throws IOException {
        try {
            return JACKSON.readValue(inputStream, ModMetadata.class);
        } catch (com.fasterxml.jackson.core.JsonParseException e) {
            throw new ModMetadataParseException("JSON5-like syntax error in " + source + ": " + e.getMessage(), e);
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            throw new ModMetadataParseException("JSON structure error in " + source + ": " + e.getMessage(), e);
        }
    }

    private static ObjectMapper createJackson() {
        JsonFactory factory = JsonFactory.builder()
            .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
            .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
            .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
            .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
            .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS)
            .enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS)
            .enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS)
            .build();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.registerModule(new ModLoadingJacksonModule());
        return mapper;
    }
}
