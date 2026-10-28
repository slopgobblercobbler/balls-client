package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Dampens incoming combat knockback (horizontal and vertical %).
 */
public class VelocityTuning extends Module {
    private final NumberSetting horizontalPct = new NumberSetting("HorizontalPct", "Horizontal knockback velocity percentage kept", 0.0, 0.0, 100.0, 5.0);
    private final NumberSetting verticalPct = new NumberSetting("VerticalPct", "Vertical knockback velocity percentage kept", 0.0, 0.0, 100.0, 5.0);
    private final NumberSetting chance = new NumberSetting("Chance", "Probability percentage that tuning applies", 100.0, 10.0, 100.0, 5.0);

    public VelocityTuning() {
        super("velocity-tuning", "Velocity Tuning", "Dampens incoming combat knockback (horizontal and vertical %)", Category.MOVEMENT);
        addSettings(horizontalPct, verticalPct, chance);
    }

    public double getHorizontalPct() { return horizontalPct.getValue(); }
    public double getVerticalPct() { return verticalPct.getValue(); }
    public double getChance() { return chance.getValue(); }

    public double tuneHorizontalVelocity(double v) {
        if (!isEnabled()) return v;
        return v * (getHorizontalPct() / 100.0);
    }

    public double tuneVerticalVelocity(double vy) {
        if (!isEnabled()) return vy;
        return vy * (getVerticalPct() / 100.0);
    }
}
