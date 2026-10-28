package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Global particle density multiplier for spells, explosions, and potion effects.
 */
public class ParticleMultiplier extends Module {
    private final NumberSetting multiplier = new NumberSetting("Multiplier", "Particle quantity multiplier", 2.5, 1.0, 10.0, 0.5);

    public ParticleMultiplier() {
        super("particle-multiplier", "Particle Multiplier", "Multiplies spawned particle density for spells and explosions", Category.RENDER);
        addSettings(multiplier);
    }

    public double getMultiplier() { return multiplier.getValue(); }
}
