package io.github.hato1883.api.game;

import io.github.hato1883.api.Identifier;

public interface IGamePhase {
    Identifier getId();                  // unique identifier for the phase
    // e.g., "BUILD", "TRADE"
}
