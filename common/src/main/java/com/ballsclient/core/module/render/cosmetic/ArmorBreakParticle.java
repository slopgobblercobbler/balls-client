package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Emits custom glass shatter crystal particle bursts when an armor piece breaks in combat.
 */
public class ArmorBreakParticle extends Module {
    private final NumberSetting particleCount = new NumberSetting("ParticleCount", "Number of glass shards spawned", 30.0, 5.0, 100.0, 5.0);
    private final ColorSetting glassColor = new ColorSetting("GlassColor", "Armor shard shatter color", 0xCCFFFFFF);

    public ArmorBreakParticle() {
        super("armor-break-particle", "Armor Break Particle", "Emits dramatic glass shattering particles when armor pieces break", Category.RENDER);
        addSettings(particleCount, glassColor);
    }

    public int getParticleCount() { return particleCount.getIntValue(); }
    public int getGlassColor() { return glassColor.getValue(); }
}
