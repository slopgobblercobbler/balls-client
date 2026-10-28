package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * RedstoneSignalViewer — Renders exact 0-15 signal strength numbers directly on dust wires and power sources.
 */
public class RedstoneSignalViewer extends Module {
    private final BooleanSetting renderNumbers = new BooleanSetting("RenderNumbers", "Renders 3D numeric text overlays on active redstone components", true);
    private final BooleanSetting colorGradient = new BooleanSetting("ColorGradient", "Shifts color tint from dark crimson (1) to brilliant scarlet (15)", true);

    public RedstoneSignalViewer() {
        super("redstone-signal-viewer", "Redstone Signal Viewer", "Renders exact 0-15 signal strength numbers directly on dust wires and power sources", Category.AUTOMATION);
        addSettings(renderNumbers, colorGradient);
    }

    public BooleanSetting getRenderNumbers() {
        return renderNumbers;
    }

    public BooleanSetting getColorGradient() {
        return colorGradient;
    }

    public static int getSignalColor(int signal) {
        int clamped = Math.max(0, Math.min(15, signal));
        if (clamped == 0) {
            return 0xFF4A0000;
        }
        int red = 0x60 + (clamped * 10);
        return (0xFF << 24) | (red << 16);
    }
}
