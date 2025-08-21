package io.github.hato1883.api.world.board;

/**
 * Represents a polygon shape for a tile, with normalized vertices and a logical origin.
 */
public class PolygonShape {
    /**
     * Vertices in [0, 1] range, as x0, y0, x1, y1, ...
     */
    public final float[] vertices;
    /**
     * Origin (center point) in [0, 1] range, as [x, y].
     */
    public final float[] origin;

    public PolygonShape(float[] vertices, float[] origin) {
        this.vertices = vertices;
        this.origin = origin;
    }
}

