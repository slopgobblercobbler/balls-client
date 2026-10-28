package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Simulates thermal rising air currents above lava, magma, and fire.
 */
public class ThermalUpdraft extends Module {
    private final NumberSetting liftSpeed = new NumberSetting("LiftSpeed", "Maximum vertical buoyant updraft velocity", 0.30, 0.1, 1.0, 0.05);
    private final NumberSetting detectionRadius = new NumberSetting("DetectionRadius", "Vertical proximity detection radius for heat sources", 6.0, 2.0, 16.0, 1.0);

    public ThermalUpdraft() {
        super("thermal-updraft", "Thermal Updraft", "Simulates thermal rising air currents above lava, magma, and fire", Category.MOVEMENT);
        addSettings(liftSpeed, detectionRadius);
    }

    public double getLiftSpeed() { return liftSpeed.getValue(); }
    public double getDetectionRadius() { return detectionRadius.getValue(); }

    public double calculateUpdraft(double distanceToHeatSource) {
        if (!isEnabled() || distanceToHeatSource > getDetectionRadius() || distanceToHeatSource < 0.0) return 0.0;
        return getLiftSpeed() * (1.0 - distanceToHeatSource / getDetectionRadius());
    }
}
