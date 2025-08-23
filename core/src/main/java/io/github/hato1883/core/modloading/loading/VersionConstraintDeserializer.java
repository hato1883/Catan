package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;

import java.io.IOException;

/**
 * Custom Jackson deserializer for VersionConstraint interface.
 */
public class VersionConstraintDeserializer extends JsonDeserializer<VersionConstraint> {
    @Override
    public VersionConstraint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_NULL) {
            throw JsonMappingException.from(p, "Version constraint is missing or null. To allow any version, use '*' or 'any'.", new IllegalArgumentException("Version constraint is missing or null. To allow any version, use '*' or 'any'."));
        }
        if (!p.hasCurrentToken() || p.currentToken().isStructStart() || p.currentToken().isStructEnd()) {
            throw new IllegalArgumentException("Version constraint must be a string. To allow any version, use '*' or 'any'.");
        }
        if (p.currentToken() != JsonToken.VALUE_STRING) {
            throw new IllegalArgumentException("Version constraint must be a string. To allow any version, use '*' or 'any'.");
        }
        String value = p.getValueAsString();
        if (value == null) {
            throw new IllegalArgumentException("Version constraint is missing or null. To allow any version, use '*' or 'any'.");
        }
        return VersionConstraint.parse(value);
    }

    @Override
    public VersionConstraint getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        throw JsonMappingException.from(ctxt.getParser(), "Version constraint is missing or null. To allow any version, use '*' or 'any'.", new IllegalArgumentException("Version constraint is missing or null. To allow any version, use '*' or 'any'."));
    }
}
