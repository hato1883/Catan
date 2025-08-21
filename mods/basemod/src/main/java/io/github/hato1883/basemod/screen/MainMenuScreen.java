package io.github.hato1883.basemod.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import io.github.hato1883.api.Identifier;
import io.github.hato1883.api.ui.screen.AbstractEventDrivenScreen;

/**
 * The main menu screen for the base mod. Uses the identifier "basemod:main_menu".
 */
public class MainMenuScreen extends AbstractEventDrivenScreen {
    private SpriteBatch batch;

    public static final Identifier ID = Identifier.of("basemod", "main_menu");

    public MainMenuScreen() {
        super(ID);
    }

    @Override
    protected void onShow() {
        batch = new SpriteBatch();
    }

    @Override
    protected void onRender(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();
        // TODO: Draw main menu UI here
        batch.end();
    }

    @Override
    protected void onDispose() {
        if (batch != null) batch.dispose();
    }
}

