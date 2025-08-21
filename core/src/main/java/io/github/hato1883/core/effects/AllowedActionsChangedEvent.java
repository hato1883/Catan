package io.github.hato1883.core.effects;

import io.github.hato1883.api.effects.IAllowedActionsChangedEvent;
import io.github.hato1883.api.effects.IEffectType;

import java.util.Set;

/**
 * Concrete implementation of IAllowedActionsChangedEvent.
 */
public class AllowedActionsChangedEvent implements IAllowedActionsChangedEvent {
    private final Set<String> allowedActions;
    private final Set<IEffectType> activeEffects;

    public AllowedActionsChangedEvent(Set<String> allowedActions, Set<IEffectType> activeEffects) {
        this.allowedActions = allowedActions;
        this.activeEffects = activeEffects;
    }

    @Override
    public Set<String> getAllowedActions() {
        return allowedActions;
    }

    @Override
    public Set<IEffectType> getActiveEffects() {
        return activeEffects;
    }
}

