package io.github.hato1883.core.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class CoordinateUtils {
    private CoordinateUtils() {}

    public static Vector2 screenToWorld(Camera cam, int screenX, int screenY) {
        Vector3 v = cam.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(v.x, v.y);
    }
}
