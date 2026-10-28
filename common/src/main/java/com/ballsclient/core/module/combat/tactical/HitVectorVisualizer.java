package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Hit Vector Visualizer — Renders 3D vectors indicating the exact direction and force of landed combat strikes
 */
public class HitVectorVisualizer extends Module {
    private final NumberSetting vectorScale = new NumberSetting("VectorScale", "Length scaling multiplier for hit vectors", 1.5, 0.5, 3.0, 0.1);
    private final NumberSetting durationTicks = new NumberSetting("DurationTicks", "Duration vector remains visible in ticks", 30.0, 10.0, 60.0, 1.0);

    public HitVectorVisualizer() {
        super("hit-vector-visualizer", "Hit Vector Visualizer", "Renders 3D vectors indicating the exact direction and force of landed combat strikes", Category.COMBAT);
        addSettings(vectorScale, durationTicks);
    }

    public double getVectorScale() {
        return vectorScale.getValue();
    }

    public double getDurationTicks() {
        return durationTicks.getValue();
    }

    public double[] computeImpactVector(double attackerEyeX, double attackerEyeY, double attackerEyeZ, double targetCenterY, double targetCenterX, double targetCenterZ, double knockbackStrength) {
        double dx = targetCenterX - attackerEyeX;
        double dy = targetCenterY - attackerEyeY;
        double dz = targetCenterZ - attackerEyeZ;
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len == 0) return new double[]{0, 0, 0};
        return new double[]{dx / len * knockbackStrength, dy / len * knockbackStrength, dz / len * knockbackStrength};
    }
}
