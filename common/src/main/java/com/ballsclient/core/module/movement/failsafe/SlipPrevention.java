package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Prevents accidental sliding off ice or edges when stopping.
 */
public class SlipPrevention extends Module {
    private final BooleanSetting zeroDrift = new BooleanSetting("ZeroDrift", "Instantly zero residual slide velocity on input release", true);
    private final NumberSetting brakePower = new NumberSetting("BrakePower", "Friction braking factor applied to residual sliding", 0.95, 0.5, 1.0, 0.01);

    public SlipPrevention() {
        super("slip-prevention", "Slip Prevention", "Prevents accidental sliding off ice or edges when stopping", Category.MOVEMENT);
        addSettings(zeroDrift, brakePower);
    }

    public boolean isZeroDrift() { return zeroDrift.getValue(); }
    public double getBrakePower() { return brakePower.getValue(); }

    public double calculateBrakeVelocity(double v, boolean isInputNeutral) {
        if (!isEnabled() || !isInputNeutral) return v;
        return isZeroDrift() ? 0.0 : v * (1.0 - getBrakePower());
    }
}
