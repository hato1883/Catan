package io.github.hato1883.core.effects;

import io.github.hato1883.api.effects.IEffectType;
import io.github.hato1883.api.effects.EffectContext;

import java.util.Optional;

/**
 * Concrete implementation of IEffectType.
 */
public class EffectType implements IEffectType {
    private final String id;
    private final IEffectType parent;

    public EffectType(String id, IEffectType parent) {
        this.id = id;
        this.parent = parent;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Optional<IEffectType> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean isEnabled(EffectContext context) {
        if (parent != null && !parent.isEnabled(context)) {
            return false;
        }
        return true;
    }
}

