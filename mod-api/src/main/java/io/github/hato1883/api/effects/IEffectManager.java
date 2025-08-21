package io.github.hato1883.api.effects;

import java.util.List;

/**
 * Manages effects. Effect events are dispatched via the global event bus (see IEventBus).
 */
public interface IEffectManager {
    void applyEffect(EffectContext context, IEffectInstance instance);
    List<IEffectInstance> getActiveEffects(EffectContext context);
    void clearEffects(EffectContext context);
    // Effect events are dispatched via the global event bus; do not use local listeners.
}
