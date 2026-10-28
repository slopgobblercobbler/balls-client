package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Neutralizes or minimizes movement restriction inside cobwebs.
 */
public class WebBypass extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Cobweb bypass mechanism", "Fast", "Fast", "Ignore", "Timer");
    private final NumberSetting factor = new NumberSetting("Factor", "Speed retention factor while inside cobweb", 0.85, 0.10, 1.0, 0.05);

    public WebBypass() {
        super("web-bypass", "Web Bypass", "Neutralizes or minimizes movement restriction inside cobwebs", Category.MOVEMENT);
        addSettings(mode, factor);
    }

    public String getMode() { return mode.getValue(); }
    public double getFactor() { return factor.getValue(); }

    public double calculateCobwebModifier(double originalModifier) {
        if (!isEnabled()) return originalModifier;
        if ("Ignore".equalsIgnoreCase(getMode())) return 1.0;
        return Math.max(originalModifier, getFactor());
    }
}
