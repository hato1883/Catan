package io.github.hato1883.api.effects;

import java.util.Map;
import java.util.Optional;

/**
 * Abstract base for all effect types. Modders should extend this.
 */
public abstract class EffectType {
    private final String id;
    private final EffectType parent;

    protected EffectType(String id, EffectType parent) {
        this.id = id;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public Optional<EffectType> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Override to provide custom disabling logic. By default, disabling parent disables children.
     */
    public boolean isEnabled(EffectContext context) {
        if (parent != null && !parent.isEnabled(context)) {
            return false;
        }
        return true;
    }
}

