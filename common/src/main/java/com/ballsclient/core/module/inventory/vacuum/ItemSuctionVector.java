package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ItemSuctionVector — Computes 3D acceleration vectors to draw ground items.
 */
public class ItemSuctionVector extends Module {
    private final NumberSetting maxVelocity = new NumberSetting("MaxVelocity", "Maximum suction velocity cap", 0.8, 0.1, 2.0, 0.1);

    public ItemSuctionVector() {
        super("item-suction-vector", "Item Suction Vector", "Computes 3D acceleration vectors to draw ground items", Category.INVENTORY);
        addSettings(maxVelocity);
    }

    public NumberSetting getMaxVelocity() {
        return maxVelocity;
    }

    public static double[] calculateSuctionVector(double dx, double dy, double dz, double speed, double maxVel) {
        double distSq = dx * dx + dy * dy + dz * dz;
        if (distSq <= 0.0001) return new double[]{0.0, 0.0, 0.0};
        double dist = Math.sqrt(distSq);
        double v = Math.min(maxVel, speed);
        return new double[]{
                (dx / dist) * v,
                (dy / dist) * v,
                (dz / dist) * v
        };
    }
}
