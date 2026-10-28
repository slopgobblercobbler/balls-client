package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ObserverPulseVisualizer — Renders 1-redstone-tick (2 game ticks) pulse propagation wavefronts from observers.
 */
public class ObserverPulseVisualizer extends Module {
    private final NumberSetting fadeDurationMs = new NumberSetting("FadeDurationMs", "Visual pulse wavefront animation persistence in milliseconds", 200.0, 50.0, 500.0, 25.0);

    public ObserverPulseVisualizer() {
        super("observer-pulse-visualizer", "Observer Pulse Visualizer", "Renders 1-redstone-tick (2 game ticks) pulse propagation wavefronts from observers", Category.AUTOMATION);
        addSettings(fadeDurationMs);
    }

    public NumberSetting getFadeDurationMs() {
        return fadeDurationMs;
    }

    public static int getPulseDurationGameTicks() {
        return 2;
    }
}
