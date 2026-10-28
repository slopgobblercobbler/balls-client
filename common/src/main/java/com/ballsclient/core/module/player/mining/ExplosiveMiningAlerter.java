package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ExplosiveMiningAlerter — Nether bed, TNT, and crystal blast radius and safety distance sensor.
 */
public class ExplosiveMiningAlerter extends Module {
    private final NumberSetting safeDistance = new NumberSetting("SafeDistance", "Minimum safe standoff distance from blast", 8.0, 4.0, 16.0, 1.0);
    private final BooleanSetting autoSneakOnDetonation = new BooleanSetting("AutoSneakOnDetonation", "Automatically sneak during explosion tick", true);
    private final BooleanSetting autoShieldEquip = new BooleanSetting("AutoShieldEquip", "Raise shield when within blast zone", true);

    public ExplosiveMiningAlerter() {
        super("explosive-mining-alerter", "Explosive Mining Alerter", "Nether bed, TNT, and crystal blast radius and safety distance sensor", Category.PLAYER);
        addSettings(safeDistance, autoSneakOnDetonation, autoShieldEquip);
    }

    public NumberSetting getSafeDistance() {
        return safeDistance;
    }

    public BooleanSetting getAutoSneakOnDetonation() {
        return autoSneakOnDetonation;
    }

    public BooleanSetting getAutoShieldEquip() {
        return autoShieldEquip;
    }

    public static boolean isBlastDanger(double explosionDist, double power, double safeDistance) {
        double blastRadius = power * 2.0;
        return explosionDist <= Math.max(blastRadius, safeDistance);
    }
}
