package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Applies harmonic pendulum sway and tilt during directional movement.
 */
public class PendulumCamera extends Module {
    private final NumberSetting oscillationFrequency = new NumberSetting("OscillationFrequency", "Pendulum sway frequency in Hz", 1.5, 0.2, 5.0, 0.1);
    private final NumberSetting amplitude = new NumberSetting("Amplitude", "Maximum camera roll tilt in degrees", 12.0, 1.0, 30.0, 1.0);
    private final NumberSetting dampingConstant = new NumberSetting("DampingConstant", "Harmonic oscillator decay damping rate", 0.05, 0.01, 0.2, 0.01);

    public PendulumCamera() {
        super("pendulum-camera", "Pendulum Camera", "Applies harmonic pendulum sway and tilt during directional movement", Category.MOVEMENT);
        addSettings(oscillationFrequency, amplitude, dampingConstant);
    }

    public double getOscillationFrequency() { return oscillationFrequency.getValue(); }
    public double getAmplitude() { return amplitude.getValue(); }
    public double getDampingConstant() { return dampingConstant.getValue(); }

    public double calculateRoll(double timeSec) {
        if (!isEnabled()) return 0.0;
        double w = 2.0 * Math.PI * getOscillationFrequency();
        return getAmplitude() * Math.exp(-getDampingConstant() * timeSec) * Math.sin(w * timeSec);
    }
}
