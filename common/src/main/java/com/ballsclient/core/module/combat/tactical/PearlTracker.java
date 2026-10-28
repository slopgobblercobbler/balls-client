package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Pearl Tracker — Predicts 3D flight trajectory and landing impact coordinates of thrown ender pearls
 */
public class PearlTracker extends Module {
    private final BooleanSetting renderTracer = new BooleanSetting("RenderTracer", "Render line from pearl to destination", true);
    private final BooleanSetting estimateArrivalTime = new BooleanSetting("EstimateArrivalTime", "Show countdown ticks until impact", true);

    public PearlTracker() {
        super("pearl-tracker", "Pearl Tracker", "Predicts 3D flight trajectory and landing impact coordinates of thrown ender pearls", Category.COMBAT);
        addSettings(renderTracer, estimateArrivalTime);
    }

    public boolean isRenderTracer() {
        return renderTracer.getValue();
    }

    public boolean isEstimateArrivalTime() {
        return estimateArrivalTime.getValue();
    }

    public double[] simulatePearlLanding(double x0, double y0, double z0, double vx0, double vy0, double vz0, double groundY, int maxTicks) {
        double x = x0, y = y0, z = z0, vx = vx0, vy = vy0, vz = vz0;
        for (int t = 0; t < maxTicks; t++) {
            x += vx; y += vy; z += vz;
            vx *= 0.99; vy = (vy - 0.03) * 0.99; vz *= 0.99;
            if (y <= groundY) return new double[]{x, groundY, z, t};
        }
        return new double[]{x, y, z, maxTicks};
    }
}
