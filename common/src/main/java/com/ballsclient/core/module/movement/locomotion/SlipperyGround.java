package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Imparts ice slipperiness physics to all solid ground surfaces.
 */
public class SlipperyGround extends Module {
    private final NumberSetting slipFactor = new NumberSetting("SlipFactor", "Friction slipperiness factor for all solid blocks", 0.98, 0.80, 0.999, 0.005);
    private final BooleanSetting conserveMomentum = new BooleanSetting("ConserveMomentum", "Retain horizontal speed across block transitions", true);

    public SlipperyGround() {
        super("slippery-ground", "Slippery Ground", "Imparts ice slipperiness physics to all solid ground surfaces", Category.MOVEMENT);
        addSettings(slipFactor, conserveMomentum);
    }

    public double getSlipFactor() { return slipFactor.getValue(); }
    public boolean isConserveMomentum() { return conserveMomentum.getValue(); }

    public double overrideSlipperiness(boolean isSolidGround, double originalSlipperiness) {
        if (!isEnabled()) return originalSlipperiness;
        return isSolidGround ? getSlipFactor() : originalSlipperiness;
    }
}
