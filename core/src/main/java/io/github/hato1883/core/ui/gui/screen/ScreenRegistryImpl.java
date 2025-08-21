package io.github.hato1883.core.ui.gui.screen;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.ui.screen.ICameraScreen;
import io.github.hato1883.api.ui.screen.ScreenRegistry;
import io.github.hato1883.api.events.screen.ScreenLifecycleListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScreenRegistryImpl implements ScreenRegistry {
    private final Map<Identifier, ICameraScreen> screens = new ConcurrentHashMap<>();
    private final Map<Identifier, Set<ScreenLifecycleListener>> listeners = new ConcurrentHashMap<>();

    @Override
    public void registerScreen(Identifier id, ICameraScreen screen) {
        screens.put(id, screen);
    }

    @Override
    public boolean switchToScreen(Identifier id) {
        // This method is a stub; actual switching is handled by ScreenManager
        return screens.containsKey(id);
    }

    @Override
    public ICameraScreen getScreen(Identifier id) {
        return screens.get(id);
    }

    @Override
    public Set<Identifier> getRegisteredScreenIds() {
        return Collections.unmodifiableSet(screens.keySet());
    }

    @Override
    public void registerScreenLifecycleListener(Identifier screenId, ScreenLifecycleListener listener) {
        listeners.computeIfAbsent(screenId, k -> new HashSet<>()).add(listener);
    }

    @Override
    public void unregisterScreenLifecycleListener(Identifier screenId, ScreenLifecycleListener listener) {
        Set<ScreenLifecycleListener> set = listeners.get(screenId);
        if (set != null) set.remove(listener);
    }

    public Set<ScreenLifecycleListener> getListeners(Identifier screenId) {
        return listeners.getOrDefault(screenId, Collections.emptySet());
    }
}

