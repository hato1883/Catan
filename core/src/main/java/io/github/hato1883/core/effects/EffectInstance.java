package io.github.hato1883.core.effects;

import io.github.hato1883.api.effects.IEffectInstance;
import io.github.hato1883.api.effects.IEffectType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Concrete implementation of IEffectInstance.
 */
public class EffectInstance implements IEffectInstance {
    private final IEffectType type;
    private final Map<String, Object> metadata;

    public EffectInstance(IEffectType type, Map<String, Object> metadata) {
        this.type = Objects.requireNonNull(type);
        this.metadata = metadata == null ? Collections.emptyMap() : new HashMap<>(metadata);
    }

    @Override
    public IEffectType getType() {
        return type;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }
}

