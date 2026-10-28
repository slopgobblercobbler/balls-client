package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Threat Assessor — Computes numerical threat rating for nearby players based on gear, HP, and weapon
 */
public class ThreatAssessor extends Module {
    private final NumberSetting scanRadius = new NumberSetting("ScanRadius", "Adversary scan radius", 16.0, 6.0, 32.0, 1.0);
    private final BooleanSetting renderThreatBadge = new BooleanSetting("RenderThreatBadge", "Render badge over entity heads", true);

    public ThreatAssessor() {
        super("threat-assessor", "Threat Assessor", "Computes numerical threat rating for nearby players based on gear, HP, and weapon", Category.COMBAT);
        addSettings(scanRadius, renderThreatBadge);
    }

    public double getScanRadius() {
        return scanRadius.getValue();
    }

    public boolean isRenderThreatBadge() {
        return renderThreatBadge.getValue();
    }

    public double calculateThreatScore(double health, double armorPoints, boolean hasTotem, boolean hasCrystal, double distance) {
        double baseScore = (armorPoints * 2.0) + health;
        if (hasTotem) baseScore += 15.0;
        if (hasCrystal) baseScore += 25.0;
        double proximityWeight = Math.max(0.2, 1.0 - (distance / 32.0));
        return baseScore * proximityWeight;
    }
}
