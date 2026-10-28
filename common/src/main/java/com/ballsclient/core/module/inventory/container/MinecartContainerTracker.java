package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * MinecartContainerTracker — Tracks moving Minecart Chest/Hopper entities and profiles item transport capacity.
 */
public class MinecartContainerTracker extends Module {
    private final NumberSetting trackingRadius = new NumberSetting("TrackingRadius", "Maximum tracking radius in blocks", 32.0, 8.0, 64.0, 4.0);

    public MinecartContainerTracker() {
        super("minecart-container-tracker", "Minecart Container Tracker", "Tracks moving Minecart Chest/Hopper entities and profiles capacity", Category.INVENTORY);
        addSettings(trackingRadius);
    }

    public NumberSetting getTrackingRadius() {
        return trackingRadius;
    }

    public static double calculateDistanceSq(double px, double py, double pz, double mx, double my, double mz) {
        double dx = px - mx;
        double dy = py - my;
        double dz = pz - mz;
        return dx * dx + dy * dy + dz * dz;
    }
}
