package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Velocity Tracker — Analyzes enemy velocity vectors to predict directional jumps and strafes
 */
public class VelocityTracker extends Module {
    private final NumberSetting trackingWindowTicks = new NumberSetting("TrackingWindowTicks", "Temporal window size in ticks for velocity sampling", 20.0, 5.0, 40.0, 1.0);
    private final BooleanSetting renderVelocityVector = new BooleanSetting("RenderVelocityVector", "Draw 3D arrow showing target movement heading", true);

    public VelocityTracker() {
        super("velocity-tracker", "Velocity Tracker", "Analyzes enemy velocity vectors to predict directional jumps and strafes", Category.COMBAT);
        addSettings(trackingWindowTicks, renderVelocityVector);
    }

    public double getTrackingWindowTicks() {
        return trackingWindowTicks.getValue();
    }

    public boolean isRenderVelocityVector() {
        return renderVelocityVector.getValue();
    }

    public double calculateKineticSpeed(double vx, double vy, double vz) {
        return Math.sqrt(vx * vx + vy * vy + vz * vz);
    }
}
