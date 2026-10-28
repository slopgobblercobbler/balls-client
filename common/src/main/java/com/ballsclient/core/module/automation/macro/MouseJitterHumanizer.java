package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

import java.util.Random;

/**
 * MouseJitterHumanizer — Injects subtle Gaussian micro-movements into macro camera rotations to evade anticheat.
 */
public class MouseJitterHumanizer extends Module {
    private final NumberSetting jitterMagnitude = new NumberSetting("JitterMagnitude", "Maximum rotational jitter amplitude in degrees", 0.2, 0.05, 1.0, 0.05);
    private final NumberSetting gaussianStdDev = new NumberSetting("GaussianStdDev", "Standard deviation for Box-Muller normal distribution", 0.5, 0.1, 2.0, 0.1);

    private final Random random = new Random();

    public MouseJitterHumanizer() {
        super("mouse-jitter-humanizer", "Mouse Jitter Humanizer", "Injects subtle Gaussian micro-movements into macro camera rotations to evade anticheat", Category.AUTOMATION);
        addSettings(jitterMagnitude, gaussianStdDev);
    }

    public NumberSetting getJitterMagnitude() {
        return jitterMagnitude;
    }

    public NumberSetting getGaussianStdDev() {
        return gaussianStdDev;
    }

    /**
     * Calculates Gaussian angle perturbation using Box-Muller transform:
     * delta = sigma * sqrt(-2 * ln(U1)) * cos(2 * pi * U2)
     */
    public static double computeGaussianDelta(double u1, double u2, double stdDev, double maxMagnitude) {
        if (u1 <= 0.0) u1 = 1e-6;
        double z0 = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
        double delta = z0 * stdDev;
        return Math.max(-maxMagnitude, Math.min(maxMagnitude, delta));
    }

    public double getNextJitter() {
        double u1 = Math.max(1e-6, random.nextDouble());
        double u2 = random.nextDouble();
        return computeGaussianDelta(u1, u2, gaussianStdDev.getValue(), jitterMagnitude.getValue());
    }
}
