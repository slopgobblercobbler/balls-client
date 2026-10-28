package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * TickSkipCounter — Tallies cumulative skipped or dropped server game ticks during player session
 * and calculates tick loss percentage.
 */
public class TickSkipCounter extends Module {
    private final BooleanSetting sessionTracking = new BooleanSetting("SessionTracking", "Tracks cumulative tick loss across entire player session", true);
    private final BooleanSetting resetOnWorldJoin = new BooleanSetting("ResetOnWorldJoin", "Resets counter on joining new world or server", true);
    private final BooleanSetting displayLossPercent = new BooleanSetting("DisplayLossPercent", "Displays percentage of expected ticks that were skipped", true);

    public TickSkipCounter() {
        super("tick-skip-counter", "Tick Skip Counter", "Tallies cumulative skipped or dropped server game ticks during player session and calculates tick loss percentage.", Category.NETWORK);
        addSettings(sessionTracking, resetOnWorldJoin, displayLossPercent);
    }

    public BooleanSetting getSessionTracking() {
        return sessionTracking;
    }

    public BooleanSetting getResetOnWorldJoin() {
        return resetOnWorldJoin;
    }

    public BooleanSetting getDisplayLossPercent() {
        return displayLossPercent;
    }
}
