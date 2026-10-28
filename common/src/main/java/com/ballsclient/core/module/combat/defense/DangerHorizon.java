package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Danger Horizon — Computes explosive lethality and blast radius of crystals, anchors, TNT, and creepers
 */
public class DangerHorizon extends Module {
    private final NumberSetting alertDistance = new NumberSetting("AlertDistance", "Explosion hazard scanning distance", 10.0, 4.0, 16.0, 0.5);
    private final NumberSetting lethalDamageThreshold = new NumberSetting("LethalDamageThreshold", "Damage threshold triggering critical evacuation warning", 18.0, 10.0, 30.0, 1.0);

    public DangerHorizon() {
        super("danger-horizon", "Danger Horizon", "Computes explosive lethality and blast radius of crystals, anchors, TNT, and creepers", Category.COMBAT);
        addSettings(alertDistance, lethalDamageThreshold);
    }

    public double getAlertDistance() {
        return alertDistance.getValue();
    }

    public double getLethalDamageThreshold() {
        return lethalDamageThreshold.getValue();
    }

    public double calculateExplosionRawDamage(double distance, double maxRadius, double maxDamage, double exposure) {
        if (distance >= maxRadius) return 0.0;
        double impact = (1.0 - distance / maxRadius) * exposure;
        return (impact * impact + impact) / 2.0 * 7.0 * maxRadius + 1.0;
    }
}
