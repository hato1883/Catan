package io.github.hato1883.core.modloading.loading;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.hato1883.api.mod.load.dependency.VersionConstraint;

import java.io.IOException;

/**
 * Custom Jackson deserializer for VersionConstraint interface.
 */
public class VersionConstraintDeserializer extends JsonDeserializer<VersionConstraint> {
    @Override
    public VersionConstraint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        return VersionConstraint.parse(value);
    }
}

