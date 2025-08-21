package io.github.hato1883.api.events.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.hato1883.api.events.IEvent;

/**
 * Fired every frame after the board and batching jobs are rendered, allowing mods to add UI elements.
 */
public class UIOverlayRenderEvent implements IEvent {
    private final Stage stage;
    private final Skin skin;

    public UIOverlayRenderEvent(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    public Stage getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }
}

