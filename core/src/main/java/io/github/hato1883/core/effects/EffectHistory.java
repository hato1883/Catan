package io.github.hato1883.core.effects;

import io.github.hato1883.api.effects.IEffectHistory;
import io.github.hato1883.api.effects.IEffectInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete implementation of IEffectHistory.
 */
public class EffectHistory implements IEffectHistory {
    private final List<IEffectInstance> history = new ArrayList<>();

    @Override
    public void add(IEffectInstance instance) {
        history.add(instance);
    }

    @Override
    public List<IEffectInstance> getHistory() {
        return Collections.unmodifiableList(history);
    }
}

