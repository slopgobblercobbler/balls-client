package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * VoidBoundaryVacuumSavior — Intercepts items falling below Y = -50 and pulls them upward before void threshold.
 */
public class VoidBoundaryVacuumSavior extends Module {
    private final NumberSetting rescueYLevel = new NumberSetting("RescueYLevel", "Y-coordinate boundary below which items are rescued", -52.0, -60.0, -40.0, 1.0);

    public VoidBoundaryVacuumSavior() {
        super("void-boundary-vacuum-savior", "Void Vacuum Savior", "Intercepts items falling below safe threshold", Category.INVENTORY);
        addSettings(rescueYLevel);
    }

    public NumberSetting getRescueYLevel() {
        return rescueYLevel;
    }

    public static boolean isRescuable(double itemY, double itemVy, double rescueFloor) {
        return itemY <= rescueFloor && itemVy < 0;
    }
}
