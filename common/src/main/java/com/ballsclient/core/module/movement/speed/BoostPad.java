package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Launchpad impulse when stepping over designated block types.
 */
public class BoostPad extends Module {
    private final NumberSetting horizontalImpulse = new NumberSetting("HorizontalImpulse", "Horizontal launch impulse magnitude", 1.5, 0.2, 4.0, 0.1);
    private final NumberSetting verticalImpulse = new NumberSetting("VerticalImpulse", "Vertical launch impulse magnitude", 0.6, 0.1, 2.0, 0.05);

    public BoostPad() {
        super("boost-pad", "Boost Pad", "Launchpad impulse when stepping over designated block types", Category.MOVEMENT);
        addSettings(horizontalImpulse, verticalImpulse);
    }

    public double getHorizontalImpulse() { return horizontalImpulse.getValue(); }
    public double getVerticalImpulse() { return verticalImpulse.getValue(); }

    public double[] applyBoostPad(float yaw) {
        if (!isEnabled()) return new double[]{0.0, 0.0, 0.0};
        double rad = Math.toRadians(yaw);
        double vx = -Math.sin(rad) * getHorizontalImpulse();
        double vz = Math.cos(rad) * getHorizontalImpulse();
        return new double[]{vx, getVerticalImpulse(), vz};
    }
}
