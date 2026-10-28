package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Instantly arrests all lateral movement momentum upon key release.
 */
public class BrakePad extends Module {
    private final NumberSetting brakeSpeed = new NumberSetting("BrakeSpeed", "Residual velocity clamp when stopping (0.0 = instant)", 0.0, 0.0, 0.3, 0.02);
    private final BooleanSetting preserveVertical = new BooleanSetting("PreserveVertical", "Preserve vertical gravity velocity while braking", true);

    public BrakePad() {
        super("brake-pad", "Brake Pad", "Instantly arrests all lateral movement momentum upon key release", Category.MOVEMENT);
        addSettings(brakeSpeed, preserveVertical);
    }

    public double getBrakeSpeed() { return brakeSpeed.getValue(); }
    public boolean isPreserveVertical() { return preserveVertical.getValue(); }

    public double applyBrake(double v, boolean keysReleased) {
        if (!isEnabled()) return v;
        return keysReleased ? getBrakeSpeed() : v;
    }
}
