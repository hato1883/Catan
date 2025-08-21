package io.github.hato1883.api.world.board;

/**
 * Represents the logical center/origin of a tile in board space.
 * Supports 3D coordinates and both Euler angles and quaternion-based rotation for extensibility.
 *
 * - Use x, y, z for position in board space.
 * - Use getYaw(), getPitch(), getRoll() for simple orientation (in degrees).
 * - Use getQuaternion() for advanced 3D/VR/AR orientation (always available).
 *
 * Regardless of how the tile was placed (Euler or Quaternion), both representations are always available.
 */
public class TilePosition implements ITilePosition{
    private final float x;
    private final float y;
    private final float z;
    private final Quaternion rotation; // always non-null

    // Position only (no rotation)
    public TilePosition(float x, float y, float z) {
        this(x, y, z, Quaternion.fromEuler(0f, 0f, 0f));
    }
    // Position + Euler
    public TilePosition(float x, float y, float z, float yaw, float pitch, float roll) {
        this(x, y, z, Quaternion.fromEuler(yaw, pitch, roll));
    }
    // Position + Quaternion
    public TilePosition(float x, float y, float z, Quaternion quaternion) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = quaternion != null ? quaternion : Quaternion.fromEuler(0f, 0f, 0f);
    }
    // Full constructor (for future extensibility)
    public TilePosition(float x, float y, float z, float yaw, float pitch, float roll, Quaternion quaternion) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = quaternion != null ? quaternion : Quaternion.fromEuler(yaw, pitch, roll);
    }


    // ***
    // * Position methods
    // ***

    /**
     * Returns the x coordinate of this tile's position.
     */
    public float x() {
        return x;
    }

    /**
     * Returns the y coordinate of this tile's position.
     */
    public float y() {
        return y;
    }

    /**
     * Returns the z coordinate of this tile's position.
     */
    public float z() {
        return z;
    }


    // ***
    // * Rotation methods
    // ***

    /**
     * Returns the quaternion representing this tile's rotation (always non-null).
     */
    public Quaternion getQuaternion() {
        return rotation;
    }

    /**
     * Returns the yaw (in degrees) for this tile's rotation.
     */
    public float getYaw() {
        return rotation.toEuler()[0];
    }
    /**
     * Returns the pitch (in degrees) for this tile's rotation.
     */
    public float getPitch() {
        return rotation.toEuler()[1];
    }
    /**
     * Returns the roll (in degrees) for this tile's rotation.
     */
    public float getRoll() {
        return rotation.toEuler()[2];
    }
}
