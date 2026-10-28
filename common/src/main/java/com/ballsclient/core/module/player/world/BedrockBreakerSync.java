package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BedrockBreakerSync — Tick-perfect piston/TNT timing synchronization for Nether ceiling bedrock felling.
 */
public class BedrockBreakerSync extends Module {
    private final ModeSetting targetMode = new ModeSetting("TargetMode", "Target bedrock layer", "CeilingY127", "CeilingY127", "FloorYMinus64", "ManualTarget");
    private final BooleanSetting autoPistonPlacement = new BooleanSetting("AutoPistonPlacement", "Automatically sequence piston retraction placement", true);
    private final NumberSetting tickCadence = new NumberSetting("TickCadence", "Piston power pulse cycle length", 2.0, 1.0, 5.0, 1.0);

    public BedrockBreakerSync() {
        super("bedrock-breaker-sync", "Bedrock Breaker Sync", "Tick-perfect piston/TNT timing synchronization for Nether ceiling bedrock felling", Category.PLAYER);
        addSettings(targetMode, autoPistonPlacement, tickCadence);
    }

    public ModeSetting getTargetMode() {
        return targetMode;
    }

    public BooleanSetting getAutoPistonPlacement() {
        return autoPistonPlacement;
    }

    public NumberSetting getTickCadence() {
        return tickCadence;
    }

    public static boolean isBedrockBreakable(int y, String targetMode) {
        if ("CeilingY127".equalsIgnoreCase(targetMode)) {
            return y == 127 || y == 126 || y == 125;
        }
        if ("FloorYMinus64".equalsIgnoreCase(targetMode)) {
            return y >= -64 && y <= -60;
        }
        return true;
    }

    public static int computeCyclePhase(int currentTick, int cadence) {
        if (cadence <= 0) return 0;
        return Math.floorMod(currentTick, cadence);
    }
}
