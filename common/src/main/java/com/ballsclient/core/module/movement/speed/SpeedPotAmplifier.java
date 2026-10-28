package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Artificially amplifies speed status effect velocity multiplier.
 */
public class SpeedPotAmplifier extends Module {
    private final NumberSetting ampFactor = new NumberSetting("AmpFactor", "Speed potion velocity boost scaling factor", 1.35, 1.0, 2.5, 0.05);
    private final BooleanSetting fieldOfViewSync = new BooleanSetting("FieldOfViewSync", "Keep camera FOV proportional to speed amplification", true);

    public SpeedPotAmplifier() {
        super("speed-pot-amplifier", "Speed Pot Amplifier", "Artificially amplifies speed status effect velocity multiplier", Category.MOVEMENT);
        addSettings(ampFactor, fieldOfViewSync);
    }

    public double getAmpFactor() { return ampFactor.getValue(); }
    public boolean isFieldOfViewSync() { return fieldOfViewSync.getValue(); }

    public double calculateAmplifiedMultiplier(int amplifierLevel) {
        if (!isEnabled()) return 1.0 + 0.20 * (amplifierLevel + 1);
        return (1.0 + 0.20 * (amplifierLevel + 1)) * getAmpFactor();
    }
}
