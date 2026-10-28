package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Sweep Attack Optimizer — Angles sword sweeps to maximize splash hit coverage across enemy clusters
 */
public class SweepAttackOptimizer extends Module {
    private final NumberSetting minClusterSize = new NumberSetting("MinClusterSize", "Minimum adjacent entities to prioritize sweeping edge", 2.0, 2.0, 5.0, 1.0);
    private final BooleanSetting requireSweepingEdge = new BooleanSetting("RequireSweepingEdge", "Check for Sweeping Edge enchantment", false);

    public SweepAttackOptimizer() {
        super("sweep-attack-optimizer", "Sweep Attack Optimizer", "Angles sword sweeps to maximize splash hit coverage across enemy clusters", Category.COMBAT);
        addSettings(minClusterSize, requireSweepingEdge);
    }

    public double getMinClusterSize() {
        return minClusterSize.getValue();
    }

    public boolean isRequireSweepingEdge() {
        return requireSweepingEdge.getValue();
    }

    public int countSweepTargets(double originX, double originZ, double[][] entityCoords, double maxSweepRadius) {
        int count = 0;
        for (double[] coord : entityCoords) {
            double dx = coord[0] - originX;
            double dz = coord[1] - originZ;
            if (Math.sqrt(dx * dx + dz * dz) <= maxSweepRadius) count++;
        }
        return count;
    }
}
