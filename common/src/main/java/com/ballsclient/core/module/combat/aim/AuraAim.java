package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Aura Aim — Multi-target combat director prioritizing threats by health, distance, and armor durability
 */
public class AuraAim extends Module {
    private final ModeSetting priorityMode = new ModeSetting("PriorityMode", "Entity prioritization algorithm", "Distance", "Distance", "Health", "ArmorDurability", "ThreatScore");
    private final NumberSetting range = new NumberSetting("Range", "Target acquisition range", 4.2, 3.0, 6.0, 0.1);
    private final BooleanSetting wallsCheck = new BooleanSetting("WallsCheck", "Raycast through blocks check", true);

    public AuraAim() {
        super("aura-aim", "Aura Aim", "Multi-target combat director prioritizing threats by health, distance, and armor durability", Category.COMBAT);
        addSettings(priorityMode, range, wallsCheck);
    }

    public String getPriorityMode() {
        return priorityMode.getValue();
    }

    public double getRange() {
        return range.getValue();
    }

    public boolean isWallsCheck() {
        return wallsCheck.getValue();
    }

    public double computeTargetPriority(double distance, double health, double armorPoints, String mode) {
        switch (mode.toLowerCase()) {
            case "health": return -health;
            case "distance": return -distance;
            case "armordurability": return -armorPoints;
            default: return -(distance * 0.5 + health * 0.5);
        }
    }
}
