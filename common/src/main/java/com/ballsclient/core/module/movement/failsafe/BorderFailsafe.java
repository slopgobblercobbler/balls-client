package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Hard-clamps velocity vectors before crossing world border boundaries.
 */
public class BorderFailsafe extends Module {
    private final NumberSetting safetyBufferBlocks = new NumberSetting("SafetyBufferBlocks", "Distance buffer in blocks before world border line", 2.0, 0.5, 10.0, 0.5);
    private final BooleanSetting soundAlert = new BooleanSetting("SoundAlert", "Play audible click warning when approaching border", true);

    public BorderFailsafe() {
        super("border-failsafe", "Border Failsafe", "Hard-clamps velocity vectors before crossing world border boundaries", Category.MOVEMENT);
        addSettings(safetyBufferBlocks, soundAlert);
    }

    public double getSafetyBufferBlocks() { return safetyBufferBlocks.getValue(); }
    public boolean isSoundAlert() { return soundAlert.getValue(); }

    public boolean isNearBorder(double playerCoord, double borderCoord) {
        return isEnabled() && Math.abs(borderCoord - playerCoord) <= getSafetyBufferBlocks();
    }

    public double clampVelocity(double v, double playerCoord, double borderCoord) {
        if (!isEnabled()) return v;
        if (isNearBorder(playerCoord, borderCoord)) {
            if ((borderCoord > playerCoord && v > 0.0) || (borderCoord < playerCoord && v < 0.0)) {
                return 0.0;
            }
        }
        return v;
    }
}
