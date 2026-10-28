package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TickFreezeDetector — Detects full server tick freeze events (e.g. crash loop, server thread hang,
 * or /tick freeze) and counts freeze duration.
 */
public class TickFreezeDetector extends Module {
    private final NumberSetting freezeThresholdMs = new NumberSetting("FreezeThresholdMs", "Time without server update triggering freeze state in milliseconds", 2500.0, 1000.0, 10000.0, 500.0);
    private final BooleanSetting autoResetTimer = new BooleanSetting("AutoResetTimer", "Automatically resets stopwatch when server tick resumes", true);
    private final BooleanSetting showStopwatch = new BooleanSetting("ShowStopwatch", "Renders elapsed freeze duration stopwatch on HUD", true);

    public TickFreezeDetector() {
        super("tick-freeze-detector", "Tick Freeze Detector", "Detects full server tick freeze events (e.g. crash loop, server thread hang, or /tick freeze) and counts freeze duration.", Category.NETWORK);
        addSettings(freezeThresholdMs, autoResetTimer, showStopwatch);
    }

    public NumberSetting getFreezeThresholdMs() {
        return freezeThresholdMs;
    }

    public BooleanSetting getAutoResetTimer() {
        return autoResetTimer;
    }

    public BooleanSetting getShowStopwatch() {
        return showStopwatch;
    }
}
