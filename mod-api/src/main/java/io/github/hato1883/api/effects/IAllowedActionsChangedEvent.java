package io.github.hato1883.api.effects;

import io.github.hato1883.api.events.IEvent;
import java.util.Set;

/**
 * Event fired when allowed actions or effects change.
 * Dispatched via the global event bus.
 */
public interface IAllowedActionsChangedEvent extends IEvent {
    Set<String> getAllowedActions();
    Set<IEffectType> getActiveEffects();
}
