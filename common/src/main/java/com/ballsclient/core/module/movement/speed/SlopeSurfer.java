package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Converts downhill elevation slope into forward skiing acceleration.
 */
public class SlopeSurfer extends Module {
    private final NumberSetting surgeFactor = new NumberSetting("SurgeFactor", "Downhill grade skiing acceleration coefficient", 1.45, 1.0, 3.0, 0.05);
    private final BooleanSetting snowSurfaceOnly = new BooleanSetting("SnowSurfaceOnly", "Only apply ski acceleration on snow or ice slopes", false);

    public SlopeSurfer() {
        super("slope-surfer", "Slope Surfer", "Converts downhill elevation slope into forward skiing acceleration", Category.MOVEMENT);
        addSettings(surgeFactor, snowSurfaceOnly);
    }

    public double getSurgeFactor() { return surgeFactor.getValue(); }
    public boolean isSnowSurfaceOnly() { return snowSurfaceOnly.getValue(); }

    public double calculateSurgeSpeed(double baseSpeed, double deltaElevation) {
        if (!isEnabled()) return baseSpeed;
        if (deltaElevation < 0.0) {
            return baseSpeed * (1.0 + Math.abs(deltaElevation) * getSurgeFactor());
        }
        return baseSpeed;
    }
}
