package io.github.hato1883.api.world.board;

public class BoardGenerationConfig {
    private final int xExtent;
    private final int yExtent;
    private final int zExtent;
    private final boolean shuffleTiles;

    public BoardGenerationConfig(int radius, boolean shuffleTiles) {
        this.xExtent = radius * 2 - 1;
        this.yExtent = radius * 2 - 1;
        this.zExtent = radius * 2 - 1;
        this.shuffleTiles = shuffleTiles;
    }

    public BoardGenerationConfig(int xExtent, int yExtent, int zExtent, boolean shuffleTiles) {
        this.xExtent = xExtent;
        this.yExtent = yExtent;
        this.zExtent = zExtent;
        this.shuffleTiles = shuffleTiles;
    }

    public BoardGenerationConfig(Dimension dimension, boolean shuffleTiles) {
        this.xExtent = dimension.x();
        this.yExtent = dimension.y();
        this.zExtent = dimension.z();
        this.shuffleTiles = shuffleTiles;
    }

    // Add other options like water tile inclusion, desert placement, etc.

    public boolean shouldShuffleTiles() { return shuffleTiles; }

    public int getxExtent() {
        return xExtent;
    }

    public int getyExtent() {
        return yExtent;
    }

    public int getzExtent() {
        return zExtent;
    }

}
