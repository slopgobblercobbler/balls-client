package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * InstamineThresholdIndicator — Evaluates efficiency, haste, and hardness inequality (speed >= 30 * H).
 */
public class InstamineThresholdIndicator extends Module {
    private final BooleanSetting hudIndicator = new BooleanSetting("HudIndicator", "Display instamine status icon on crosshair HUD", true);
    private final BooleanSetting playSoundOnReady = new BooleanSetting("PlaySoundOnReady", "Play audio chime when instamine threshold reached", true);
    private final ColorSetting readyColor = new ColorSetting("ReadyColor", "Crosshair color when instamine active", 0xFF00FF00);

    public InstamineThresholdIndicator() {
        super("instamine-threshold-indicator", "Instamine Indicator", "Evaluates efficiency, haste, and hardness inequality (speed >= 30 * H)", Category.PLAYER);
        addSettings(hudIndicator, playSoundOnReady, readyColor);
    }

    public BooleanSetting getHudIndicator() {
        return hudIndicator;
    }

    public BooleanSetting getPlaySoundOnReady() {
        return playSoundOnReady;
    }

    public ColorSetting getReadyColor() {
        return readyColor;
    }

    public static double calculateEffectiveSpeed(double baseSpeed, int effLevel, int hasteLevel) {
        double toolSpeed = baseSpeed;
        if (effLevel > 0) {
            toolSpeed += (effLevel * effLevel + 1.0);
        }
        if (hasteLevel > 0) {
            toolSpeed *= (1.0 + 0.2 * hasteLevel);
        }
        return toolSpeed;
    }

    public static boolean canInstamine(double baseSpeed, int effLevel, int hasteLevel, double blockHardness, boolean isProperTool) {
        if (!isProperTool) return false;
        double effectiveSpeed = calculateEffectiveSpeed(baseSpeed, effLevel, hasteLevel);
        return effectiveSpeed >= (30.0 * blockHardness);
    }
}
