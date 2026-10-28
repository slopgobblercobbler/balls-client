package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Custom decorative aesthetic particle trails following player footsteps (Flames, Musical Notes, Runes).
 */
public class TrailParticles extends Module {
    private final ModeSetting trailType = new ModeSetting("Type", "Particle aesthetic style", "FLAME", "FLAME", "NOTES", "ENCHANT", "PORTAL");
    private final NumberSetting density = new NumberSetting("Density", "Particles spawned per footstep tick", 3.0, 1.0, 10.0, 1.0);

    public TrailParticles() {
        super("trail-particles", "Trail Particles", "Emits decorative particle trails behind player footsteps", Category.RENDER);
        addSettings(trailType, density);
    }

    public String getTrailType() { return trailType.getValue(); }
    public int getDensity() { return density.getIntValue(); }
}
