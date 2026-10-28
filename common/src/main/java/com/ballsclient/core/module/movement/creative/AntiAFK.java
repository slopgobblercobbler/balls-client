package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Humanized micro-movements, yaw sweeps, and jumps to avoid AFK kick.
 */
public class AntiAFK extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "AFK avoidance behavioral pattern", "RandomPath", "RandomPath", "Spin", "Walk", "Jump");
    private final NumberSetting intervalSeconds = new NumberSetting("IntervalSeconds", "Seconds between anti-AFK behavioral cycles", 45.0, 10.0, 300.0, 5.0);
    private final BooleanSetting humanize = new BooleanSetting("Humanize", "Add gaussian noise to motion to simulate human input", true);

    public AntiAFK() {
        super("anti-afk", "Anti AFK", "Humanized micro-movements, yaw sweeps, and jumps to avoid AFK kick", Category.MOVEMENT);
        addSettings(mode, intervalSeconds, humanize);
    }

    public String getMode() { return mode.getValue(); }
    public double getIntervalSeconds() { return intervalSeconds.getValue(); }
    public boolean isHumanize() { return humanize.getValue(); }

    public boolean shouldPerformAction(long elapsedMs) {
        return isEnabled() && elapsedMs >= (long) (getIntervalSeconds() * 1000.0);
    }
}
