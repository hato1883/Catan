package io.github.hato1883.api.registries;

import io.github.hato1883.api.events.ui.UIRenderEvent;
import io.github.hato1883.api.events.IEventListener;
import java.util.List;

/**
 * Registry for main-thread UI rendering event handlers. Only handlers for UIRenderEvent are allowed.
 * All processing should be done before this event is fired; handlers should only render.
 */
public interface IUIHandlerRegistry extends IRegistry<IEventListener<UIRenderEvent>> {
    /**
     * Returns all handlers registered for UI rendering events.
     */
    List<IEventListener<UIRenderEvent>> getHandlersForEvent();
}
