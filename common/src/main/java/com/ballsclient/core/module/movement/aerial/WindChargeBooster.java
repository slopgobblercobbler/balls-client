package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Maximizes kinetic launch recoil from Wind Charge explosions.
 */
public class WindChargeBooster extends Module {
    private final NumberSetting recoilMultiplier = new NumberSetting("RecoilMultiplier", "Vertical launch boost multiplier", 1.6, 1.0, 3.0, 0.05);
    private final BooleanSetting autoCrouch = new BooleanSetting("AutoCrouch", "Auto-crouch on landing to absorb shock wave", true);

    public WindChargeBooster() {
        super("wind-charge-booster", "Wind Charge Booster", "Maximizes kinetic launch recoil from Wind Charge explosions", Category.MOVEMENT);
        addSettings(recoilMultiplier, autoCrouch);
    }

    public double getRecoilMultiplier() { return recoilMultiplier.getValue(); }
    public boolean isAutoCrouch() { return autoCrouch.getValue(); }

    public double boostRecoil(double vy) {
        if (!isEnabled()) return vy;
        return vy * getRecoilMultiplier();
    }
}
