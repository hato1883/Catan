package io.github.hato1883.api.mod.load;

import java.util.List;

/** Calls onInitialize on each mod with proper context classloader handling. */
public interface IModInitializer {
    void initializeAll(List<ILoadedMod> mods);
}
