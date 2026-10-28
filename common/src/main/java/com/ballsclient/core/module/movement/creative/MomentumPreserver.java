package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Retains kinetic velocity across medium boundaries (water/air/web).
 */
public class MomentumPreserver extends Module {
    private final NumberSetting retentionRatio = new NumberSetting("RetentionRatio", "Kinetic energy retention ratio across medium boundaries", 0.95, 0.5, 1.0, 0.01);
    private final BooleanSetting crossMedium = new BooleanSetting("CrossMedium", "Preserve velocity when plunging into water or cobweb", true);

    public MomentumPreserver() {
        super("momentum-preserver", "Momentum Preserver", "Retains kinetic velocity across medium boundaries (water/air/web)", Category.MOVEMENT);
        addSettings(retentionRatio, crossMedium);
    }

    public double getRetentionRatio() { return retentionRatio.getValue(); }
    public boolean isCrossMedium() { return crossMedium.getValue(); }

    public double preserveEnergy(double kineticEnergy) {
        if (!isEnabled()) return kineticEnergy;
        return kineticEnergy * getRetentionRatio();
    }
}
