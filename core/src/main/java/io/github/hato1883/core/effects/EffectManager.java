package io.github.hato1883.core.effects;

import io.github.hato1883.api.effects.*;
import io.github.hato1883.api.events.IEventBus;
import java.util.*;

/**
 * Manages application, removal, and querying of effects.
 * Effect events are dispatched via the global event bus.
 */
public class EffectManager implements IEffectManager {
    private final Map<EffectContext, IEffectHistory> contextHistories = new HashMap<>();
    private final IEventBus eventBus;

    public EffectManager(IEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void applyEffect(EffectContext context, IEffectInstance instance) {
        IEffectHistory history = contextHistories.computeIfAbsent(context, k -> new EffectHistory());
        history.add(instance);
        emitAllowedActionsChanged(context);
    }

    @Override
    public List<IEffectInstance> getActiveEffects(EffectContext context) {
        IEffectHistory history = contextHistories.get(context);
        if (history == null) return Collections.emptyList();
        return history.getHistory();
    }

    @Override
    public void clearEffects(EffectContext context) {
        contextHistories.remove(context);
        emitAllowedActionsChanged(context);
    }

    private void emitAllowedActionsChanged(EffectContext context) {
        Set<String> allowedActions = new HashSet<>(); // TODO: derive from effects
        Set<IEffectType> activeTypes = new HashSet<>();
        for (IEffectInstance inst : getActiveEffects(context)) {
            if (inst.getType().isEnabled(context)) {
                activeTypes.add(inst.getType());
            }
        }
        IAllowedActionsChangedEvent event = new AllowedActionsChangedEvent(allowedActions, activeTypes);
        eventBus.dispatch(event);
    }
}
