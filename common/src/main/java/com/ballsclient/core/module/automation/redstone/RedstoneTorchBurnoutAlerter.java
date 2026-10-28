package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * RedstoneTorchBurnoutAlerter — Monitors torch toggle frequencies and alerts when approaching 8 toggles / 60 ticks.
 */
public class RedstoneTorchBurnoutAlerter extends Module {
    private final NumberSetting burnoutThreshold = new NumberSetting("BurnoutThreshold", "Toggle count warning limit within rolling 60 ticks", 7.0, 4.0, 8.0, 1.0);

    public RedstoneTorchBurnoutAlerter() {
        super("redstone-torch-burnout-alerter", "Torch Burnout Alerter", "Monitors torch toggle frequencies and alerts when approaching 8 toggles / 60 ticks", Category.AUTOMATION);
        addSettings(burnoutThreshold);
    }

    public NumberSetting getBurnoutThreshold() {
        return burnoutThreshold;
    }

    public boolean isApproachingBurnout(int togglesInWindow) {
        return togglesInWindow >= burnoutThreshold.getValue();
    }
}
