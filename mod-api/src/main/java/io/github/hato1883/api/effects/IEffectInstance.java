package io.github.hato1883.api.effects;

import java.util.Map;

public interface IEffectInstance {
    IEffectType getType();
    Map<String, Object> getMetadata();
}
