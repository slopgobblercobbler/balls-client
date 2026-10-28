package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Neutralizes soul sand/soil slow effect with Soul Speed scaling.
 */
public class SoulSandSpeed extends Module {
    private final BooleanSetting bypassSlowness = new BooleanSetting("BypassSlowness", "Neutralize vanilla 0.4x soul sand speed reduction", true);
    private final NumberSetting boostMultiplier = new NumberSetting("BoostMultiplier", "Additional velocity factor on soul sand", 1.3, 1.0, 2.5, 0.05);

    public SoulSandSpeed() {
        super("soul-sand-speed", "Soul Sand Speed", "Neutralizes soul sand/soil slow effect with Soul Speed scaling", Category.MOVEMENT);
        addSettings(bypassSlowness, boostMultiplier);
    }

    public boolean isBypassSlowness() { return bypassSlowness.getValue(); }
    public double getBoostMultiplier() { return boostMultiplier.getValue(); }

    public double modifySpeed(double baseSpeed, int soulSpeedLevel) {
        if (!isEnabled()) return baseSpeed;
        double s = isBypassSlowness() ? baseSpeed * (1.0 / 0.4) : baseSpeed;
        s *= (1.0 + 0.105 * soulSpeedLevel);
        return s * getBoostMultiplier();
    }
}
