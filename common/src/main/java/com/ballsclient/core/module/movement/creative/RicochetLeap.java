package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Mid-air wall-kicking off vertical surfaces to chain multi-leaps.
 */
public class RicochetLeap extends Module {
    private final NumberSetting kickImpulse = new NumberSetting("KickImpulse", "Horizontal impulse reflected away from wall", 0.65, 0.2, 1.5, 0.05);
    private final NumberSetting maxKicks = new NumberSetting("MaxKicks", "Maximum consecutive wall-kicks before touching ground", 3.0, 1.0, 8.0, 1.0);

    public RicochetLeap() {
        super("ricochet-leap", "Ricochet Leap", "Mid-air wall-kicking off vertical surfaces to chain multi-leaps", Category.MOVEMENT);
        addSettings(kickImpulse, maxKicks);
    }

    public double getKickImpulse() { return kickImpulse.getValue(); }
    public double getMaxKicks() { return maxKicks.getValue(); }

    public double[] calculateRicochetVector(double nx, double ny, double nz, int kicksRemaining) {
        if (!isEnabled() || kicksRemaining <= 0) return new double[]{0.0, 0.0, 0.0};
        return new double[]{nx * getKickImpulse(), 0.42, nz * getKickImpulse()};
    }
}
