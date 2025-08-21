package io.github.hato1883.api.effects;

import java.util.List;

public interface IEffectHistory {
    void add(IEffectInstance instance);
    List<IEffectInstance> getHistory();
}
