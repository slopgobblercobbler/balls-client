package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Steers pathing away from fire blocks and campfires.
 */
public class AntiFire extends Module {
    private final BooleanSetting autoExtinguishBucket = new BooleanSetting("AutoExtinguishBucket", "Auto-use water bucket on player when on fire", true);
    private final NumberSetting evasionRadius = new NumberSetting("EvasionRadius", "Proximity repulsion radius from fire hazards", 2.5, 1.0, 6.0, 0.5);

    public AntiFire() {
        super("anti-fire", "Anti Fire", "Steers pathing away from fire blocks and campfires", Category.MOVEMENT);
        addSettings(autoExtinguishBucket, evasionRadius);
    }

    public boolean isAutoExtinguishBucket() { return autoExtinguishBucket.getValue(); }
    public double getEvasionRadius() { return evasionRadius.getValue(); }

    public boolean isHazardNear(double distanceToFire) {
        return isEnabled() && distanceToFire <= getEvasionRadius();
    }
}
