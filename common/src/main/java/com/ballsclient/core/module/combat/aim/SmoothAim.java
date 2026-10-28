package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Smooth Aim — Algorithmic humanized cursor easing towards target avoiding snap detection using exponential decay curves
 */
public class SmoothAim extends Module {
    private final NumberSetting smoothFactor = new NumberSetting("SmoothFactor", "Aim smoothing interpolation factor", 12.0, 1.0, 50.0, 0.5);
    private final NumberSetting deadzone = new NumberSetting("Deadzone", "Angular deadzone in degrees", 0.5, 0.0, 5.0, 0.1);
    private final ModeSetting easingType = new ModeSetting("EasingType", "Mathematical easing curve", "Exponential", "Exponential", "Linear", "Cosine");

    public SmoothAim() {
        super("smooth-aim", "Smooth Aim", "Algorithmic humanized cursor easing towards target avoiding snap detection using exponential decay curves", Category.COMBAT);
        addSettings(smoothFactor, deadzone, easingType);
    }

    public double getSmoothFactor() {
        return smoothFactor.getValue();
    }

    public double getDeadzone() {
        return deadzone.getValue();
    }

    public String getEasingType() {
        return easingType.getValue();
    }

    public double computeSmoothedAngle(double currentAngle, double targetAngle, double smoothFactor, double maxDelta) {
        double diff = Math.IEEEremainder(targetAngle - currentAngle, 360.0);
        if (Math.abs(diff) < 0.1) return targetAngle;
        double step = diff / Math.max(1.0, smoothFactor);
        if (Math.abs(step) > maxDelta) step = Math.signum(step) * maxDelta;
        return currentAngle + step;
    }
}
