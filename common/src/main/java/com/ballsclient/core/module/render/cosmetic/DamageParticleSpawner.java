package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Spawns customized particle fountains bursting upon landing strikes against enemies.
 */
public class DamageParticleSpawner extends Module {
    private final ModeSetting particleType = new ModeSetting("Type", "Particle aesthetic style", "MAGIC", "CRIT", "MAGIC", "FIREWORKS", "HEARTS");
    private final NumberSetting count = new NumberSetting("Count", "Particle count emitted per strike", 25.0, 5.0, 100.0, 5.0);

    public DamageParticleSpawner() {
        super("damage-particle-spawner", "Damage Particle Spawner", "Spawns particle fountains on enemy hit impacts", Category.RENDER);
        addSettings(particleType, count);
    }

    public String getParticleType() { return particleType.getValue(); }
    public int getParticleCount() { return count.getIntValue(); }
}
