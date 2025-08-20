package io.github.hato1883.core.ui.gui.rendering;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public final class DebugRenderer {
    public void draw(PolygonSprite sprite, ShapeRenderer sr) {
        float x = sprite.getX();
        float y = sprite.getY();
        float w = sprite.getWidth();
        float h = sprite.getHeight();

        float originWorldX = x + sprite.getOriginX();
        float originWorldY = y + sprite.getOriginY();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(x, y, w, h);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLUE);
        sr.circle(originWorldX, originWorldY, 10f);
        sr.end();
    }
}
