package io.github.hato1883.api.world.board;

import com.badlogic.gdx.math.Vector2;

public final class CubeCordUtils {
    private CubeCordUtils() {}

    public static Vector2 cubeToWorld(ITilePosition coord, float radius, float gap) {
        float width = (float) (Math.sqrt(3) * radius);
        float height = 2f * radius;

        float xSpacing = width + gap;
        float ySpacing = height * 0.75f + gap;

        float x = xSpacing * (coord.x() + coord.y() / 2f);
        float y = ySpacing * -coord.y();

        return new Vector2(x, y);
    }
}
