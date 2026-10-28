package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * OreVeinCostCalculator — Computes expected resource yield per cubic chunk volume excavated.
 */
public class OreVeinCostCalculator extends Module {
    private final ModeSetting targetResource = new ModeSetting("TargetResource", "Target mining mineral type", "Diamond", "Diamond", "AncientDebris", "Emerald");
    private final NumberSetting chunkDepth = new NumberSetting("ChunkDepth", "Vertical layer thickness in evaluation", 64.0, 16.0, 128.0, 8.0);

    public OreVeinCostCalculator() {
        super("ore-vein-cost-calculator", "Ore Vein Cost Calculator", "Computes expected resource yield per cubic chunk volume excavated", Category.PLAYER);
        addSettings(targetResource, chunkDepth);
    }

    public ModeSetting getTargetResource() {
        return targetResource;
    }

    public NumberSetting getChunkDepth() {
        return chunkDepth;
    }

    public static double predictYield(int blocksExcavated, double probability) {
        return blocksExcavated * probability;
    }
}
