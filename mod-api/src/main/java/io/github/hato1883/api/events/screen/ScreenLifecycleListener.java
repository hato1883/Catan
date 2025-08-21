package io.github.hato1883.api.events.screen;

/**
 * Interface for listening to screen lifecycle events in the Catan modding API.
 * <p>
 * <b>When to use:</b> Only use this interface if you want to extend or interact with a screen you do not own—such as
 * adding overlays, custom UI, or advanced rendering to core screens (e.g., {@code basemod:main_menu},
 * {@code basemod:game_screen}) or other mods' screens. If you have registered your own screen, you should implement all
 * logic directly in your {@code ICameraScreen} implementation and not use this event system for your own screen.
 * <p>
 * For overlays, UI, or simple mod rendering, see the documentation for {@code UIOverlayRenderEvent} and
 * {@code IBatchingJob} for easier and safer alternatives.
 * <p>
 * <b>Screen Navigation:</b> To open or switch screens, use the {@code ScreenManager} API and refer to screens by their
 * {@code Identifier}. This allows you to add buttons or UI elements that open your own or other screens.
 * <p>
 * <b>Registration:</b> Register your listener for a specific screen using an {@code Identifier} with
 * {@code ScreenRegistry.registerScreenLifecycleListener(Identifier, ScreenLifecycleListener)}. You do not need to check
 * the screen ID in your event methods.
 * <p>
 * <b>Important:</b> Do <u>not</u> call {@code Gdx.gl.glClearColor(...)} or {@code Gdx.gl.glClear(...)} in your render
 * method. The framework already clears the screen before dispatching render events. Calling these methods yourself can
 * and will erase assets drawn by other mods.
 * <p>
 * <b>Camera Access:</b> The event provides access to the current {@code ICameraScreen} via {@code event.getScreen()}.
 * You can directly call {@code getCamera()} on this object. <b>Do not create or resize your own camera</b>; the
 * framework manages camera resizing in the screen's resize method. Creating or resizing your own camera can cause
 * rendering issues and conflicts with other mods.
 * <p>
 * <b>Example usage:</b>
 * <pre>{@code
 * // Registration (in your mod init code):
 * ScreenRegistry registry = ...;
 * Identifier screenId = new Identifier("basemod", "main_menu");
 * registry.registerScreenLifecycleListener(screenId, new MyMainMenuOverlayListener());
 *
 * // Listener implementation:
 * public class MyMainMenuOverlayListener implements ScreenLifecycleListener {
 *     private SpriteBatch batch;
 *
 *     @Override
 *     public void onScreenShow(ScreenShowEvent event) {
 *         batch = new SpriteBatch();
 *     }
 *
 *     @Override
 *     public void onScreenRender(ScreenRenderEvent event) {
 *         // Do not clear the screen here!
 *         var camera = event.getScreen().getCamera();
 *         batch.setProjectionMatrix(camera.combined);
 *         batch.begin();
 *         // ... custom rendering ...
 *         batch.end();
 *     }
 *
 *     @Override
 *     public void onScreenDispose(ScreenDisposeEvent event) {
 *         batch.dispose();
 *     }
 * }
 * }</pre>
 */
public interface ScreenLifecycleListener {
    /**
     * Called when the screen is shown. Use this to allocate resources.
     */
    void onScreenShow(ScreenShowEvent event);
    /**
     * Called when the screen is disposed. Use this to clean up resources.
     */
    void onScreenDispose(ScreenDisposeEvent event);
    /**
     * Called every frame after the screen has been cleared. Do not clear the screen again here.
     */
    default void onScreenRender(ScreenRenderEvent event) {}
    /**
     * Called when the screen is resized. Use this to update resource sizes or respond to camera changes.
     */
    default void onScreenResize(ScreenResizeEvent event) {}
    /**
     * Called when the screen is paused. Use this to pause animations, music, or save state.
     */
    default void onScreenPause(ScreenPauseEvent event) {}
    /**
     * Called when the screen is resumed. Use this to resume animations, music, or reload state.
     */
    default void onScreenResume(ScreenResumeEvent event) {}
    // Add more lifecycle hooks as needed
}
