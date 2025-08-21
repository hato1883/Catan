package io.github.hato1883.api.effects;

import java.util.Optional;

public interface IEffectType {
    String getId();
    Optional<IEffectType> getParent();
    boolean isEnabled(EffectContext context);
}
