package io.github.hato1883.api.events.ui;

/**
 * Options for configuring a batch for a mod's batching job.
 */
public class BatchOptions {
    private boolean emittersCleanUpBlendFunction = true;
    // Add more options as needed

    public boolean isEmittersCleanUpBlendFunction() {
        return emittersCleanUpBlendFunction;
    }

    public BatchOptions setEmittersCleanUpBlendFunction(boolean value) {
        this.emittersCleanUpBlendFunction = value;
        return this;
    }
}

