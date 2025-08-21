package io.github.hato1883.api.world.board;

/**
 * Represents a position in board space. This interface is now a marker for positions.
 * All grid-specific logic is handled by ITileGrid and its subinterfaces.
 *
 * Use TilePosition for 3D position and rotation.
 */
public interface ITilePosition {
    // Position methods
    float x();
    float y();
    float z();

    // Rotation methods
    // Quaternion representation (always available)
    Quaternion getQuaternion();
    // Euler angles
    float getYaw();
    float getPitch();
    float getRoll();
}
