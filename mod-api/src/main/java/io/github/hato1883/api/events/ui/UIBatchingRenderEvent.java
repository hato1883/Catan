package io.github.hato1883.api.events.ui;

import io.github.hato1883.api.events.IEvent;

/**
 * Fired every frame for each batch type, allowing mods to render custom UI using the provided batch.
 */
public class UIBatchingRenderEvent implements IEvent {
    private final BatchType batchType;
    private final Object batch;
    private final Object renderContext;

    public UIBatchingRenderEvent(BatchType batchType, Object batch, Object renderContext) {
        this.batchType = batchType;
        this.batch = batch;
        this.renderContext = renderContext;
    }

    public BatchType getBatchType() {
        return batchType;
    }

    public Object getBatch() {
        return batch;
    }

    public Object getRenderContext() {
        return renderContext;
    }
}

