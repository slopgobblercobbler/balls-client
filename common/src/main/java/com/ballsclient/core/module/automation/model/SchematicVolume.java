package com.ballsclient.core.module.automation.model;

import java.util.*;

/**
 * Headless volumetric representation of a block structure schematic.
 * Holds dimensions, block state palette, and flat 1D index array for O(1) coordinate queries.
 * Provides Bill-of-Materials (BOM) calculation and 90-degree coordinate rotations.
 */
public class SchematicVolume {
    private final int width;
    private final int height;
    private final int length;
    private final List<String> palette;
    private final int[] blockIndices;

    public SchematicVolume(int width, int height, int length, List<String> palette, int[] blockIndices) {
        if (width <= 0 || height <= 0 || length <= 0) {
            throw new IllegalArgumentException("Schematic dimensions must be positive");
        }
        this.width = width;
        this.height = height;
        this.length = length;
        this.palette = palette != null ? new ArrayList<>(palette) : new ArrayList<>(Collections.singletonList("minecraft:air"));
        if (this.palette.isEmpty()) {
            this.palette.add("minecraft:air");
        }
        int total = width * height * length;
        if (blockIndices != null && blockIndices.length == total) {
            this.blockIndices = blockIndices.clone();
        } else {
            this.blockIndices = new int[total];
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int getTotalBlocks() {
        return width * height * length;
    }

    public List<String> getPalette() {
        return Collections.unmodifiableList(palette);
    }

    public int[] getBlockIndices() {
        return blockIndices.clone();
    }

    public int getIndex(int x, int y, int z) {
        return y * (width * length) + z * width + x;
    }

    public boolean isWithinBounds(int x, int y, int z) {
        return x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < length;
    }

    public String getBlockState(int x, int y, int z) {
        if (!isWithinBounds(x, y, z)) {
            return "minecraft:air";
        }
        int idx = blockIndices[getIndex(x, y, z)];
        if (idx >= 0 && idx < palette.size()) {
            return palette.get(idx);
        }
        return "minecraft:air";
    }

    public void setBlockState(int x, int y, int z, String blockState) {
        if (!isWithinBounds(x, y, z)) {
            return;
        }
        if (blockState == null) {
            blockState = "minecraft:air";
        }
        int paletteIndex = palette.indexOf(blockState);
        if (paletteIndex == -1) {
            palette.add(blockState);
            paletteIndex = palette.size() - 1;
        }
        blockIndices[getIndex(x, y, z)] = paletteIndex;
    }

    /**
     * Computes the bill of materials for this volume, excluding air blocks.
     *
     * @return map of block state identifier to block count
     */
    public Map<String, Integer> calculateMaterialList() {
        Map<String, Integer> counts = new HashMap<>();
        for (int idx : blockIndices) {
            if (idx >= 0 && idx < palette.size()) {
                String block = palette.get(idx);
                if (block != null && !block.equals("minecraft:air") && !block.equals("air")) {
                    counts.put(block, counts.getOrDefault(block, 0) + 1);
                }
            }
        }
        return Collections.unmodifiableMap(counts);
    }

    /**
     * Rotates volume 90 degrees clockwise around the Y axis.
     * Dimensions transform: (width, height, length) -> (length, height, width).
     */
    public SchematicVolume rotate90() {
        int newWidth = length;
        int newHeight = height;
        int newLength = width;
        int[] newIndices = new int[newWidth * newHeight * newLength];

        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    int oldIdx = getIndex(x, y, z);
                    int newX = length - 1 - z;
                    int newZ = x;
                    int newIdx = y * (newWidth * newLength) + newZ * newWidth + newX;
                    newIndices[newIdx] = blockIndices[oldIdx];
                }
            }
        }
        return new SchematicVolume(newWidth, newHeight, newLength, palette, newIndices);
    }

    /**
     * Rotates volume 180 degrees around the Y axis.
     */
    public SchematicVolume rotate180() {
        return rotate90().rotate90();
    }

    /**
     * Rotates volume 270 degrees clockwise (90 counter-clockwise) around the Y axis.
     */
    public SchematicVolume rotate270() {
        return rotate90().rotate90().rotate90();
    }

    /**
     * Mirrors volume along the specified axes.
     */
    public SchematicVolume mirror(boolean flipX, boolean flipZ) {
        int[] newIndices = new int[width * height * length];
        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++) {
                    int oldIdx = getIndex(x, y, z);
                    int targetX = flipX ? width - 1 - x : x;
                    int targetZ = flipZ ? length - 1 - z : z;
                    int targetIdx = y * (width * length) + targetZ * width + targetX;
                    newIndices[targetIdx] = blockIndices[oldIdx];
                }
            }
        }
        return new SchematicVolume(width, height, length, palette, newIndices);
    }
}
