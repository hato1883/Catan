package io.github.hato1883.core.registries;

import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.events.IEventListener;
import io.github.hato1883.api.events.ui.UIRenderEvent;
import io.github.hato1883.api.registries.IUIHandlerRegistry;
import io.github.hato1883.api.events.registry.RegistryRegisterEvent;
import io.github.hato1883.api.events.registry.RegistryReplaceEvent;
import io.github.hato1883.api.events.registry.RegistryUnregisterEvent;

import java.util.*;

/**
 * Registry for main-thread UI rendering event handlers. Only handlers for UIRenderEvent are allowed.
 * All processing should be done before this event is fired; handlers should only render.
 */
public class UIHandlerRegistry extends Registry<IEventListener<UIRenderEvent>> implements IUIHandlerRegistry {
    public UIHandlerRegistry(io.github.hato1883.api.events.IEventBusService eventBus) {
        super(eventBus);
    }

    @Override
    public List<IEventListener<UIRenderEvent>> getHandlersForEvent() {
        return new ArrayList<>(getAll());
    }

    @Override
    protected RegistryRegisterEvent<IEventListener<UIRenderEvent>> createRegistryRegisterEvent(Identifier id, IEventListener<UIRenderEvent> element) {
        // No-op or custom event if desired
        return new RegistryRegisterEvent<>(this, id, element);
    }

    @Override
    protected RegistryReplaceEvent<IEventListener<UIRenderEvent>> createRegistryReplaceEvent(Identifier id, IEventListener<UIRenderEvent> oldElement, IEventListener<UIRenderEvent> newElement) {
        // No-op or custom event if desired
        return new RegistryReplaceEvent<>(this, id, oldElement, newElement);
    }

    @Override
    protected RegistryUnregisterEvent<IEventListener<UIRenderEvent>> createRegistryUnregisterEvent(Identifier id, IEventListener<UIRenderEvent> element) {
        // No-op or custom event if desired
        return new RegistryUnregisterEvent<>(this, id, element);
    }
}
