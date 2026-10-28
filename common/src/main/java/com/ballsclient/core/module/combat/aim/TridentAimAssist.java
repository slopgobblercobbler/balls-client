package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Trident Aim Assist — Dedicated ballistic trajectory solver for thrown Loyalty/Riptide tridents
 */
public class TridentAimAssist extends Module {
    private final NumberSetting tridentVelocity = new NumberSetting("TridentVelocity", "Initial trident launch velocity", 2.5, 1.0, 3.5, 0.1);
    private final BooleanSetting predictTargetMotion = new BooleanSetting("PredictTargetMotion", "Lead moving targets", true);

    public TridentAimAssist() {
        super("trident-aim-assist", "Trident Aim Assist", "Dedicated ballistic trajectory solver for thrown Loyalty/Riptide tridents", Category.COMBAT);
        addSettings(tridentVelocity, predictTargetMotion);
    }

    public double getTridentVelocity() {
        return tridentVelocity.getValue();
    }

    public boolean isPredictTargetMotion() {
        return predictTargetMotion.getValue();
    }

    public double calculateTridentPitch(double horizDist, double vertDist, double v0, double gravity) {
        double v2 = v0 * v0;
        double v4 = v2 * v2;
        double g = gravity;
        double term = v4 - g * (g * horizDist * horizDist + 2 * vertDist * v2);
        if (term < 0) return -45.0; // unreachable fallback
        return -Math.toDegrees(Math.atan((v2 - Math.sqrt(term)) / (g * horizDist)));
    }
}
