package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Dampens gravitational downward acceleration for smooth gliding.
 */
public class Glide extends Module {
    private final NumberSetting fallVelocity = new NumberSetting("FallVelocity", "Terminal downward gliding velocity", -0.12, -0.5, -0.01, 0.01);
    private final NumberSetting horizontalFriction = new NumberSetting("HorizontalFriction", "Air friction factor applied to horizontal speed", 0.99, 0.90, 1.0, 0.01);
    private final BooleanSetting waterLandSafety = new BooleanSetting("WaterLandSafety", "Deactivate glide damping above water to allow diving", true);

    public Glide() {
        super("glide", "Glide", "Dampens gravitational downward acceleration for smooth gliding", Category.MOVEMENT);
        addSettings(fallVelocity, horizontalFriction, waterLandSafety);
    }

    public double getFallVelocity() { return fallVelocity.getValue(); }
    public double getHorizontalFriction() { return horizontalFriction.getValue(); }
    public boolean isWaterLandSafety() { return waterLandSafety.getValue(); }

    public double clampGlideVelocity(double currentVy) {
        if (!isEnabled()) return currentVy;
        if (currentVy < getFallVelocity()) {
            return getFallVelocity();
        }
        return currentVy;
    }
}
