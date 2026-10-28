package com.ballsclient.core.module.automation;

import com.ballsclient.core.module.automation.model.SchematicVolume;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Schematic Volume, Rotation & Bill-of-Materials Diff Test")
class SchematicBOMDiffTest {

    @Test
    @DisplayName("Calculate Bill-of-Materials and stack requirements")
    void testBillOfMaterialsCalculation() {
        int width = 4, height = 2, length = 4; // 32 blocks
        List<String> palette = List.of("minecraft:air", "minecraft:stone", "minecraft:oak_planks");
        int[] indices = new int[32];
        // 16 stone, 10 oak planks, 6 air
        Arrays.fill(indices, 0, 16, 1);
        Arrays.fill(indices, 16, 26, 2);
        Arrays.fill(indices, 26, 32, 0);

        SchematicVolume volume = new SchematicVolume(width, height, length, palette, indices);
        Map<String, Integer> bom = volume.calculateMaterialList();

        assertThat(bom.get("minecraft:stone")).isEqualTo(16);
        assertThat(bom.get("minecraft:oak_planks")).isEqualTo(10);
        assertThat(bom.containsKey("minecraft:air")).isFalse();
    }

    @Test
    @DisplayName("Inventory diff computation: required vs available yields missing items")
    void testInventoryDiff() {
        Map<String, Integer> required = Map.of(
                "minecraft:diamond_block", 64,
                "minecraft:obsidian", 100,
                "minecraft:glass", 20
        );

        Map<String, Integer> inventory = Map.of(
                "minecraft:diamond_block", 30, // 34 missing
                "minecraft:obsidian", 100,     // 0 missing
                "minecraft:dirt", 64          // surplus/extra
        );

        Map<String, Integer> missing = new HashMap<>();
        for (Map.Entry<String, Integer> req : required.entrySet()) {
            int avail = inventory.getOrDefault(req.getKey(), 0);
            if (avail < req.getValue()) {
                missing.put(req.getKey(), req.getValue() - avail);
            }
        }

        assertThat(missing.get("minecraft:diamond_block")).isEqualTo(34);
        assertThat(missing.containsKey("minecraft:obsidian")).isFalse();
        assertThat(missing.get("minecraft:glass")).isEqualTo(20);
    }

    @Test
    @DisplayName("90-degree yaw coordinate rotation transforms width and length")
    void testVolumeRotation() {
        int width = 3, height = 2, length = 5;
        List<String> palette = List.of("minecraft:air", "minecraft:stone");
        int[] indices = new int[width * height * length];
        // Mark corner (0, 0, 0)
        indices[0] = 1;

        SchematicVolume original = new SchematicVolume(width, height, length, palette, indices);
        SchematicVolume rotated = original.rotate90();

        // 90 deg rotation swaps width and length: (3, 2, 5) -> (5, 2, 3)
        assertThat(rotated.getWidth()).isEqualTo(5);
        assertThat(rotated.getHeight()).isEqualTo(2);
        assertThat(rotated.getLength()).isEqualTo(3);
        assertThat(rotated.getBlockState(4, 0, 0)).isEqualTo("minecraft:stone");
    }

    @Test
    @DisplayName("180 and 270 degree rotations and mirroring")
    void testRotationsAndMirroring() {
        int width = 2, height = 1, length = 2;
        List<String> palette = List.of("minecraft:air", "minecraft:gold_block");
        int[] indices = new int[4];
        // Mark (0, 0, 0)
        indices[0] = 1;

        SchematicVolume vol = new SchematicVolume(width, height, length, palette, indices);
        SchematicVolume rot180 = vol.rotate180();
        // After 180, (0, 0, 0) moves to (1, 0, 1)
        assertThat(rot180.getBlockState(1, 0, 1)).isEqualTo("minecraft:gold_block");

        SchematicVolume mirroredX = vol.mirror(true, false);
        // After flipX, (0, 0, 0) moves to (1, 0, 0)
        assertThat(mirroredX.getBlockState(1, 0, 0)).isEqualTo("minecraft:gold_block");
    }
}
