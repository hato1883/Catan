package io.github.hato1883.api.events.ui;

import io.github.hato1883.api.events.IEvent;

/**
 * Event for main-thread UI rendering. Contains rendering context and batch queue for optimal batching.
 * All processing should be done before this event is fired; handlers should only render.
 */
public class UIRenderEvent implements IEvent {
    private final Object renderContext; // Replace with actual context type if available
    private final Object batchQueue;    // Replace with actual BatchQueue type if available

    public UIRenderEvent(Object renderContext, Object batchQueue) {
        this.renderContext = renderContext;
        this.batchQueue = batchQueue;
    }

    public Object getRenderContext() {
        return renderContext;
    }

    public Object getBatchQueue() {
        return batchQueue;
    }
}

