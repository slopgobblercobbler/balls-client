package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Charges kinetic spring while crouching; releases massive leap.
 */
public class SuperJumpCharge extends Module {
    private final NumberSetting maxChargeTicks = new NumberSetting("MaxChargeTicks", "Crouch duration in ticks for maximum spring compression", 40.0, 10.0, 100.0, 5.0);
    private final NumberSetting maxLaunchForce = new NumberSetting("MaxLaunchForce", "Peak launch vertical velocity impulse", 2.5, 1.0, 6.0, 0.25);

    public SuperJumpCharge() {
        super("super-jump-charge", "Super Jump Charge", "Charges kinetic spring while crouching; releases massive leap", Category.MOVEMENT);
        addSettings(maxChargeTicks, maxLaunchForce);
    }

    public double getMaxChargeTicks() { return maxChargeTicks.getValue(); }
    public double getMaxLaunchForce() { return maxLaunchForce.getValue(); }

    public double calculateLaunchForce(int chargeTicks) {
        if (!isEnabled()) return 0.42;
        double q = Math.min(1.0, chargeTicks / getMaxChargeTicks());
        return 0.42 + q * getMaxLaunchForce();
    }
}
