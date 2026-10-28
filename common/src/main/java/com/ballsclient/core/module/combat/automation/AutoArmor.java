package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Armor — Automatically equips and upgrades armor pieces from inventory into armor slots
 */
public class AutoArmor extends Module {
    private final NumberSetting swapDelayMs = new NumberSetting("SwapDelayMs", "Delay in milliseconds between armor swaps", 50.0, 0.0, 200.0, 10.0);
    private final BooleanSetting preferElytra = new BooleanSetting("PreferElytra", "Equip elytra over chestplate when gliding", false);
    private final BooleanSetting inventoryOnlyWhenStill = new BooleanSetting("InventoryOnlyWhenStill", "Suppress armor equipping while moving to avoid desync", false);

    public AutoArmor() {
        super("auto-armor", "Auto Armor", "Automatically equips and upgrades armor pieces from inventory into armor slots", Category.COMBAT);
        addSettings(swapDelayMs, preferElytra, inventoryOnlyWhenStill);
    }

    public double getSwapDelayMs() {
        return swapDelayMs.getValue();
    }

    public boolean isPreferElytra() {
        return preferElytra.getValue();
    }

    public boolean isInventoryOnlyWhenStill() {
        return inventoryOnlyWhenStill.getValue();
    }

    public double evaluateArmorPieceScore(int defense, int toughness, int protLevel) {
        return (defense * 1.5) + (toughness * 2.0) + (protLevel * 1.25);
    }

    public int selectBestPieceSlot(double[] pieceScores, double currentEquippedScore) {
        int bestIndex = -1;
        double maxScore = currentEquippedScore;
        for (int i = 0; i < pieceScores.length; i++) {
            if (pieceScores[i] > maxScore) {
                maxScore = pieceScores[i];
                bestIndex = i;
            }
        }
        return bestIndex;
    }
}
