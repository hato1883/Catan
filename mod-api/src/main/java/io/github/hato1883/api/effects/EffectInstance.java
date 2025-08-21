package io.github.hato1883.api.effects;

import java.util.Map;

/**
 * Represents an instance of an effect with metadata.
 */
public interface EffectInstance {
    EffectType getType();
    Map<String, Object> getMetadata();
}
