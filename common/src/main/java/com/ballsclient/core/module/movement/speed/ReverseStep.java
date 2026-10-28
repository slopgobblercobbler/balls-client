package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Pulls player downward instantly when walking off block edges.
 */
public class ReverseStep extends Module {
    private final NumberSetting downSpeed = new NumberSetting("DownSpeed", "Downward snap velocity pulling player to surface", 1.0, 0.2, 3.0, 0.1);
    private final NumberSetting maxDropDistance = new NumberSetting("MaxDropDistance", "Maximum vertical ledge drop height in blocks", 3.0, 1.0, 6.0, 0.5);

    public ReverseStep() {
        super("reverse-step", "Reverse Step", "Pulls player downward instantly when walking off block edges", Category.MOVEMENT);
        addSettings(downSpeed, maxDropDistance);
    }

    public double getDownSpeed() { return downSpeed.getValue(); }
    public double getMaxDropDistance() { return maxDropDistance.getValue(); }

    public boolean shouldReverseStep(boolean onGround, double dropDistance) {
        return isEnabled() && !onGround && dropDistance > 0.0 && dropDistance <= getMaxDropDistance();
    }
}
