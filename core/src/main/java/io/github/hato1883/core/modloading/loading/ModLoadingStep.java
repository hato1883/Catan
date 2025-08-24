package io.github.hato1883.core.modloading.loading;

/**
 * Represents a single step in the mod loading pipeline.
 */
public interface ModLoadingStep {
    /**
     * Executes this step, mutating the context as needed.
     * @param context The shared mod loading context.
     * @throws Exception if the step fails (should be handled by the pipeline)
     */
    void execute(ModLoadingContext context) throws Exception;
}

