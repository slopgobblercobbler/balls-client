package com.ballsclient.core.module.render.esp;

import com.ballsclient.core.event.Render3DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Predator-style thermal vision shader mapping entity velocities to dynamic heat colors.
 */
public class ThermalVisionESP extends Module {
    private final NumberSetting heatSensitivity = new NumberSetting("Sensitivity", "Heat gradient responsiveness to movement velocity", 1.0, 0.1, 3.0, 0.1);
    private final ColorSetting hotColor = new ColorSetting("HotColor", "Color for fast-moving entities (Infrared Red)", 0xFFFF2200);
    private final ColorSetting coldColor = new ColorSetting("ColdColor", "Color for stationary entities (Cryo Blue)", 0xFF0088FF);

    public ThermalVisionESP() {
        super("thermal-vision-esp", "Thermal Vision ESP", "Velocity-based thermal heat signature shading on entities", Category.RENDER);
        addSettings(heatSensitivity, hotColor, coldColor);
    }

    public int calculateHeatColor(double velocityBps) {
        double factor = Math.min(1.0, (velocityBps / 10.0) * heatSensitivity.getValue());
        int rHot = hotColor.getRed(), gHot = hotColor.getGreen(), bHot = hotColor.getBlue();
        int rCold = coldColor.getRed(), gCold = coldColor.getGreen(), bCold = coldColor.getBlue();

        int r = (int) (rCold + (rHot - rCold) * factor);
        int g = (int) (gCold + (gHot - gCold) * factor);
        int b = (int) (bCold + (bHot - bCold) * factor);

        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    public double getHeatSensitivity() { return heatSensitivity.getValue(); }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        // Render thermal shader pass
    }
}
