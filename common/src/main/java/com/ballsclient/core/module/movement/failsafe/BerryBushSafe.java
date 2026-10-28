package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Eliminates prick damage and movement drag inside berry bushes.
 */
public class BerryBushSafe extends Module {
    private final BooleanSetting passThrough = new BooleanSetting("PassThrough", "Neutralize 0.8x movement drag in sweet berry bushes", true);
    private final NumberSetting speedCompensation = new NumberSetting("SpeedCompensation", "Compensatory velocity boost inside bushes", 1.4, 1.0, 2.5, 0.1);

    public BerryBushSafe() {
        super("berry-bush-safe", "Berry Bush Safe", "Eliminates prick damage and movement drag inside berry bushes", Category.MOVEMENT);
        addSettings(passThrough, speedCompensation);
    }

    public boolean isPassThrough() { return passThrough.getValue(); }
    public double getSpeedCompensation() { return speedCompensation.getValue(); }

    public double compensateBushSpeed(double speed) {
        if (!isEnabled()) return speed;
        return isPassThrough() ? speed * getSpeedCompensation() : speed;
    }
}
