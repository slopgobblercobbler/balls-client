package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AFKDriftNeutralizer — Detects water and entity collision drift and applies neutralizing counter-vectors.
 */
public class AFKDriftNeutralizer extends Module {
    private final NumberSetting driftTolerance = new NumberSetting("DriftTolerance", "Threshold velocity drift before applying correction", 0.01, 0.001, 0.1, 0.005);
    private final BooleanSetting applyCounterForce = new BooleanSetting("ApplyCounterForce", "Dispatch movement correction input to stay stationary", true);
    private final BooleanSetting alertPlayer = new BooleanSetting("AlertPlayer", "Send notification if player displaced beyond 1 block", false);

    public AFKDriftNeutralizer() {
        super("afk-drift-neutralizer", "AFK Drift Neutralizer", "Detects water and entity collision drift and applies neutralizing counter-vectors", Category.PLAYER);
        addSettings(driftTolerance, applyCounterForce, alertPlayer);
    }

    public NumberSetting getDriftTolerance() {
        return driftTolerance;
    }

    public BooleanSetting getApplyCounterForce() {
        return applyCounterForce;
    }

    public BooleanSetting getAlertPlayer() {
        return alertPlayer;
    }

    public static double[] getCounterVelocity(double vx, double vz, double tolerance) {
        double mag = Math.sqrt(vx * vx + vz * vz);
        if (mag > tolerance) {
            return new double[]{-vx, -vz};
        }
        return new double[]{0.0, 0.0};
    }
}
