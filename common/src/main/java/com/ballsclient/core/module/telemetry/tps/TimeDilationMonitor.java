package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * TimeDilationMonitor — Measures game world slow-motion ratio when server runs below 20 TPS,
 * calculating player physical speed scaling factor.
 */
public class TimeDilationMonitor extends Module {
    private final ModeSetting displayFormat = new ModeSetting("DisplayFormat", "Time dilation display format", "PERCENT_SPEED", "PERCENT_SPEED", "RATIO_MULTIPLIER", "SLOWDOWN_FACTOR");
    private final BooleanSetting warnSevereDilation = new BooleanSetting("WarnSevereDilation", "Triggers warning when dilation slows world below 50% speed", true);
    private final BooleanSetting showEquivalentSeconds = new BooleanSetting("ShowEquivalentSeconds", "Displays real seconds required for one in-game second", false);

    public TimeDilationMonitor() {
        super("time-dilation-monitor", "Time Dilation Monitor", "Measures game world slow-motion ratio when server runs below 20 TPS, calculating player physical speed scaling factor.", Category.NETWORK);
        addSettings(displayFormat, warnSevereDilation, showEquivalentSeconds);
    }

    public ModeSetting getDisplayFormat() {
        return displayFormat;
    }

    public BooleanSetting getWarnSevereDilation() {
        return warnSevereDilation;
    }

    public BooleanSetting getShowEquivalentSeconds() {
        return showEquivalentSeconds;
    }
}
