package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Skybox star density multiplier with twinkling clusters and periodic shooting stars.
 */
public class StarfieldDensity extends Module {
    private final NumberSetting starCountMultiplier = new NumberSetting("Multiplier", "Density factor for night sky stars", 3.0, 1.0, 10.0, 0.5);
    private final BooleanSetting shootingStars = new BooleanSetting("ShootingStars", "Spawn periodic shooting meteor streaks across sky", true);

    public StarfieldDensity() {
        super("starfield-density", "Starfield Density", "Multiplies celestial skybox star count and adds shooting stars", Category.RENDER);
        addSettings(starCountMultiplier, shootingStars);
    }

    public double getStarCountMultiplier() { return starCountMultiplier.getValue(); }
    public boolean isShootingStars() { return shootingStars.getValue(); }
}
