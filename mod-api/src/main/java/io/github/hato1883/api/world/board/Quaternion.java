package io.github.hato1883.api.world.board;

/**
 * Represents a quaternion for 3D rotation.
 * Used for advanced tile orientation in 3D/VR/AR or modded boards.
 */
public class Quaternion {
    public final float x, y, z, w;

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Creates a Quaternion from Euler angles (in degrees, yaw-pitch-roll order).
     */
    public static Quaternion fromEuler(float yaw, float pitch, float roll) {
        // Convert degrees to radians
        double cy = Math.cos(Math.toRadians(yaw) * 0.5);
        double sy = Math.sin(Math.toRadians(yaw) * 0.5);
        double cp = Math.cos(Math.toRadians(pitch) * 0.5);
        double sp = Math.sin(Math.toRadians(pitch) * 0.5);
        double cr = Math.cos(Math.toRadians(roll) * 0.5);
        double sr = Math.sin(Math.toRadians(roll) * 0.5);

        float w = (float)(cr * cp * cy + sr * sp * sy);
        float x = (float)(sr * cp * cy - cr * sp * sy);
        float y = (float)(cr * sp * cy + sr * cp * sy);
        float z = (float)(cr * cp * sy - sr * sp * cy);
        return new Quaternion(x, y, z, w);
    }

    /**
     * Converts this quaternion to Euler angles (in degrees, yaw-pitch-roll order).
     * Returns a float array: [yaw, pitch, roll]
     */
    public float[] toEuler() {
        // Reference: https://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles
        float ysqr = y * y;

        // roll (x-axis rotation)
        float t0 = +2.0f * (w * x + y * z);
        float t1 = +1.0f - 2.0f * (x * x + ysqr);
        float roll = (float)Math.toDegrees(Math.atan2(t0, t1));

        // pitch (y-axis rotation)
        float t2 = +2.0f * (w * y - z * x);
        t2 = Math.min(t2, 1.0f);
        t2 = Math.max(t2, -1.0f);
        float pitch = (float)Math.toDegrees(Math.asin(t2));

        // yaw (z-axis rotation)
        float t3 = +2.0f * (w * z + x * y);
        float t4 = +1.0f - 2.0f * (ysqr + z * z);
        float yaw = (float)Math.toDegrees(Math.atan2(t3, t4));

        return new float[] {yaw, pitch, roll};
    }

    @Override
    public String toString() {
        return "Quaternion{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
