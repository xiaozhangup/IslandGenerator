package ch.mixin.islandgenerator.islandGeneration;

public class SpawnRange {
    private final int maxHeight;
    private final int minHeight;

    public SpawnRange(int maxHeight, int minHeight) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinHeight() {
        return minHeight;
    }
}
