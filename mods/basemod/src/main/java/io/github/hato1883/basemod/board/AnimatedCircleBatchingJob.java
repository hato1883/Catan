package io.github.hato1883.basemod.board;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.hato1883.api.events.ui.BatchType;
import io.github.hato1883.api.events.ui.BatchingContext;
import io.github.hato1883.api.events.ui.IBatchingJob;

public class AnimatedCircleBatchingJob implements IBatchingJob {
    private float angle = 0f;
    private final float radius;
    private final float circleRadius;
    private final float speed;

    public static boolean showRedBall = true;

    public AnimatedCircleBatchingJob(float radius, float circleRadius, float speed) {
        this.radius = radius;
        this.circleRadius = circleRadius;
        this.speed = speed;
    }

    @Override
    public void render(BatchingContext ctx) {
        if (!showRedBall) return;
        // Advance the angle for animation
        angle += speed; // speed should be small, e.g., 0.02f
        if (angle > (Math.PI * 2)) angle -= (float) (Math.PI * 2);
        // Use the camera's current position as the center
        float centerX = 0f;
        float centerY = 0f;
        if (ctx.getRenderContext() != null) {
            OrthographicCamera camera = (OrthographicCamera) ctx.getRenderContext();
            centerX = camera.position.x;
            centerY = camera.position.y;
        }
        float x = centerX + radius * (float)Math.cos(angle);
        float y = centerY + radius * (float)Math.sin(angle);
        ShapeRenderer sr = (ShapeRenderer) ctx.getBatch();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1, 0, 0, 0.5f); // semi-transparent red
        sr.circle(x, y, circleRadius);
        sr.end();
    }

    @Override
    public BatchType getBatchType() {
        return BatchType.SHAPE;
    }
}
