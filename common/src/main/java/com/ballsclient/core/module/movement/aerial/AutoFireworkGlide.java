package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto-fires rockets at optimal -40° pitch to maximize glide distance.
 */
public class AutoFireworkGlide extends Module {
    private final NumberSetting optimalPitch = new NumberSetting("OptimalPitch", "Optimal glide pitch angle in degrees", -40.0, -60.0, -20.0, 1.0);
    private final NumberSetting speedThreshold = new NumberSetting("SpeedThreshold", "Speed threshold to trigger firework use", 0.75, 0.2, 2.0, 0.05);
    private final BooleanSetting autoSlotSwitch = new BooleanSetting("AutoSlotSwitch", "Automatically select firework rocket slot", true);
    private final BooleanSetting pitchLock = new BooleanSetting("PitchLock", "Lock pitch angle during rocket propulsion", true);

    public AutoFireworkGlide() {
        super("auto-firework-glide", "Auto Firework Glide", "Auto-fires rockets at optimal -40° pitch to maximize glide distance", Category.MOVEMENT);
        addSettings(optimalPitch, speedThreshold, autoSlotSwitch, pitchLock);
    }

    public double getOptimalPitch() { return optimalPitch.getValue(); }
    public double getSpeedThreshold() { return speedThreshold.getValue(); }
    public boolean isAutoSlotSwitch() { return autoSlotSwitch.getValue(); }
    public boolean isPitchLock() { return pitchLock.getValue(); }

    public boolean shouldUseRocket(double horizontalSpeed) {
        return isEnabled() && horizontalSpeed < getSpeedThreshold();
    }

    public double[] calculateLookVector(float yaw) {
        double pRad = Math.toRadians(getOptimalPitch());
        double yRad = Math.toRadians(yaw);
        double ux = -Math.sin(yRad) * Math.cos(pRad);
        double uy = -Math.sin(pRad);
        double uz = Math.cos(yRad) * Math.cos(pRad);
        return new double[]{ux, uy, uz};
    }
}
