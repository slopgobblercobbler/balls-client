package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Nullifies directional camera tilt and screen shaking upon taking damage.
 */
public class DamageTiltCompensation extends Module {
    private final BooleanSetting dampenTilt = new BooleanSetting("DampenTilt", "Cancel camera hurt tilt rotation", true);
    private final NumberSetting recoilCompensation = new NumberSetting("RecoilCompensation", "Recoil reduction factor from incoming attacks", 0.80, 0.0, 1.0, 0.05);

    public DamageTiltCompensation() {
        super("damage-tilt-compensation", "Damage Tilt Compensation", "Nullifies directional camera tilt and screen shaking upon taking damage", Category.MOVEMENT);
        addSettings(dampenTilt, recoilCompensation);
    }

    public boolean isDampenTilt() { return dampenTilt.getValue(); }
    public double getRecoilCompensation() { return recoilCompensation.getValue(); }

    public float calculateTiltAngle(float originalTilt) {
        if (!isEnabled()) return originalTilt;
        return isDampenTilt() ? 0.0f : originalTilt * (float)(1.0 - getRecoilCompensation());
    }
}
