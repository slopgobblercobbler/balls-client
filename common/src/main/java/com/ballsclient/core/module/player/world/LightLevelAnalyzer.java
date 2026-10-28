package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * LightLevelAnalyzer — Evaluates block and sky light levels for 1.18+ vs legacy hostile mob spawning.
 */
public class LightLevelAnalyzer extends Module {
    private final ModeSetting spawnRule = new ModeSetting("SpawnRule", "Mob spawn light rule engine", "Modern_1_18_Plus", "Modern_1_18_Plus", "Legacy_1_17_Minus");
    private final BooleanSetting blockLightOnly = new BooleanSetting("BlockLightOnly", "Evaluate only artificial block light sources", false);
    private final ColorSetting dangerColor = new ColorSetting("DangerColor", "Color indicator for spawnable blocks", 0xFFFF0000);

    public LightLevelAnalyzer() {
        super("light-level-analyzer", "Light Level Analyzer", "Evaluates block and sky light levels for 1.18+ vs legacy hostile mob spawning", Category.PLAYER);
        addSettings(spawnRule, blockLightOnly, dangerColor);
    }

    public ModeSetting getSpawnRule() {
        return spawnRule;
    }

    public BooleanSetting getBlockLightOnly() {
        return blockLightOnly;
    }

    public ColorSetting getDangerColor() {
        return dangerColor;
    }

    public static boolean canHostileSpawn(int blockLight, int skyLight, boolean isModern) {
        if (isModern) {
            // In 1.18+, hostile mobs only spawn at block light 0 (or sky light 0 at night)
            return blockLight == 0 && skyLight <= 7;
        } else {
            // Legacy 1.17-: hostile mobs spawn when total effective light <= 7
            int totalLight = Math.max(blockLight, skyLight);
            return totalLight <= 7;
        }
    }
}
