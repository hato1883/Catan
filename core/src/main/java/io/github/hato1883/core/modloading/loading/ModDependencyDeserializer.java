package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.hato1883.api.mod.load.dependency.ModDependency;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;
import java.io.IOException;

/**
 * Custom deserializer for ModDependency to map 'id' and 'version' fields from JSON.
 */
public class ModDependencyDeserializer extends JsonDeserializer<ModDependency> {
    @Override
    public ModDependency deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String modId = node.has("id") ? node.get("id").asText() : null;
        VersionConstraint versionConstraint = null;
        if (node.has("version")) {
            String versionStr = node.get("version").asText();
            versionConstraint = VersionConstraint.parse(versionStr);
        }
        Boolean optional = node.has("optional") && node.get("optional").asBoolean();
        return new ModDependency(modId, versionConstraint, optional);
    }
}

