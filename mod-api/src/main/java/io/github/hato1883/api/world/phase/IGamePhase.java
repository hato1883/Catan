package io.github.hato1883.api.world.phase;

import io.github.hato1883.api.Identifier;

public interface IGamePhase {
    Identifier getId();                  // unique identifier for the phase
    // e.g., "BUILD", "TRADE"
}
