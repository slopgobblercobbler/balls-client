package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * GroundDespawnPriorityPickup — Tracks ground item age (0-6000 ticks) and prioritizes suction towards items near despawn.
 */
public class GroundDespawnPriorityPickup extends Module {
    private final NumberSetting criticalAgeTicks = new NumberSetting("CriticalAgeTicks", "Item age threshold considered critical", 5400.0, 4000.0, 5900.0, 100.0);

    public GroundDespawnPriorityPickup() {
        super("ground-despawn-priority-pickup", "Despawn Priority Pickup", "Prioritizes suction towards items near despawn", Category.INVENTORY);
        addSettings(criticalAgeTicks);
    }

    public NumberSetting getCriticalAgeTicks() {
        return criticalAgeTicks;
    }

    public static boolean isNearDespawn(int ageTicks, int criticalThreshold) {
        return ageTicks >= criticalThreshold;
    }

    public static double calculateDespawnUrgencyScore(int ageTicks, double distance) {
        return ageTicks / Math.max(1.0, distance);
    }
}
