package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * MagneticItemCollector — Spherical magnet utility expanding item pickup reach radius up to 8.0 meters.
 */
public class MagneticItemCollector extends Module {
    private final NumberSetting pickupExpansionRadius = new NumberSetting("PickupExpansionRadius", "Expanded magnetic reach in blocks", 2.5, 0.5, 5.0, 0.5);

    public MagneticItemCollector() {
        super("magnetic-item-collector", "Magnetic Item Collector", "Spherical magnet utility expanding item pickup reach", Category.INVENTORY);
        addSettings(pickupExpansionRadius);
    }

    public NumberSetting getPickupExpansionRadius() {
        return pickupExpansionRadius;
    }

    public static boolean isInMagneticReach(double distanceSq, double reachRadius) {
        return distanceSq <= (reachRadius * reachRadius);
    }
}
