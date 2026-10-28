package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Blast Protection Evaluator — Calculates post-armor explosion damage using EPF scaling and distance attenuation
 */
public class BlastProtectionEvaluator extends Module {
    private final NumberSetting blastProtLevel = new NumberSetting("BlastProtLevel", "Enchantment level of blast protection", 8.0, 0.0, 16.0, 1.0);
    private final BooleanSetting considerToughness = new BooleanSetting("ConsiderToughness", "Factor in armor toughness calculation", true);

    public BlastProtectionEvaluator() {
        super("blast-protection-evaluator", "Blast Protection Evaluator", "Calculates post-armor explosion damage using EPF scaling and distance attenuation", Category.COMBAT);
        addSettings(blastProtLevel, considerToughness);
    }

    public double getBlastProtLevel() {
        return blastProtLevel.getValue();
    }

    public boolean isConsiderToughness() {
        return considerToughness.getValue();
    }

    public double calculateNetBlastDamage(double rawExplosionDamage, double armorDefense, double toughness, int blastProtEPF) {
        double defenseFactor = Math.min(20.0, Math.max(armorDefense / 5.0, armorDefense - rawExplosionDamage / (2.0 + toughness / 4.0)));
        double postArmorDamage = rawExplosionDamage * (1.0 - defenseFactor / 25.0);
        int clampedEPF = Math.min(20, Math.max(0, blastProtEPF));
        double finalDamage = postArmorDamage * (1.0 - clampedEPF / 25.0);
        return Math.max(0.0, finalDamage);
    }
}
