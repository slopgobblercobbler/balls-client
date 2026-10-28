package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Predicts high-velocity ground impact and auto-deploys water/hay.
 */
public class SafeLanding extends Module {
    private final NumberSetting predictDistance = new NumberSetting("PredictDistance", "Lookahead distance in blocks for landing surface", 4.0, 1.0, 10.0, 0.5);
    private final BooleanSetting deployWater = new BooleanSetting("DeployWater", "Auto-place water bucket at impact point", true);
    private final BooleanSetting deployHay = new BooleanSetting("DeployHay", "Prioritize placing hay bale to dampen impact", false);

    public SafeLanding() {
        super("safe-landing", "Safe Landing", "Predicts high-velocity ground impact and auto-deploys water/hay", Category.MOVEMENT);
        addSettings(predictDistance, deployWater, deployHay);
    }

    public double getPredictDistance() { return predictDistance.getValue(); }
    public boolean isDeployWater() { return deployWater.getValue(); }
    public boolean isDeployHay() { return deployHay.getValue(); }

    public boolean shouldDeployLandingAid(double fallDistance, double timeToImpact) {
        return isEnabled() && fallDistance >= 3.5 && timeToImpact <= 1.0;
    }
}
