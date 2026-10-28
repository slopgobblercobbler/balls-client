package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Crosshair Target Tracker — Dynamic HUD reticle tracking primary opponent velocity, distance, and armor status
 */
public class CrosshairTargetTracker extends Module {
    private final NumberSetting trackingFov = new NumberSetting("TrackingFov", "Maximum tracking field of view", 60.0, 10.0, 120.0, 1.0);
    private final BooleanSetting renderDistance = new BooleanSetting("RenderDistance", "Display distance text next to reticle", true);
    private final BooleanSetting renderReticle = new BooleanSetting("RenderReticle", "Render locking diamond overlay", true);

    public CrosshairTargetTracker() {
        super("crosshair-target-tracker", "Crosshair Target Tracker", "Dynamic HUD reticle tracking primary opponent velocity, distance, and armor status", Category.COMBAT);
        addSettings(trackingFov, renderDistance, renderReticle);
    }

    public double getTrackingFov() {
        return trackingFov.getValue();
    }

    public boolean isRenderDistance() {
        return renderDistance.getValue();
    }

    public boolean isRenderReticle() {
        return renderReticle.getValue();
    }

    public double calculateTrackingConfidence(double angleToCrosshair, double maxFov, double distance, double maxDistance) {
        if (angleToCrosshair > maxFov || distance > maxDistance) return 0.0;
        double angleWeight = 1.0 - (angleToCrosshair / maxFov);
        double distWeight = 1.0 - (distance / maxDistance);
        return Math.max(0.0, Math.min(1.0, 0.6 * angleWeight + 0.4 * distWeight));
    }
}
