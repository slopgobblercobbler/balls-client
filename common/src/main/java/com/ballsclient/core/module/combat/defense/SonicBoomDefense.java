package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Sonic Boom Defense — Predicts Warden sonic boom charges and incoming unblockable magical damage
 */
public class SonicBoomDefense extends Module {
    private final NumberSetting alertDistance = new NumberSetting("AlertDistance", "Threat distance for Warden sonic attack", 15.0, 5.0, 20.0, 1.0);
    private final BooleanSetting autoEgress = new BooleanSetting("AutoEgress", "Steer player behind cover during sonic charge", true);

    public SonicBoomDefense() {
        super("sonic-boom-defense", "Sonic Boom Defense", "Predicts Warden sonic boom charges and incoming unblockable magical damage", Category.COMBAT);
        addSettings(alertDistance, autoEgress);
    }

    public double getAlertDistance() {
        return alertDistance.getValue();
    }

    public boolean isAutoEgress() {
        return autoEgress.getValue();
    }

    public boolean isSonicBoomImminent(int wardenAnimationTick, double distance) {
        return wardenAnimationTick >= 25 && wardenAnimationTick <= 34 && distance <= 16.0;
    }
}
