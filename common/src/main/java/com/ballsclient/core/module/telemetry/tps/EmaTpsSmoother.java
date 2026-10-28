package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * EmaTpsSmoother — Applies Exponential Moving Average (EMA) filtering (alpha = 2 / (N + 1))
 * to eliminate noisy micro-fluctuations in server TPS.
 */
public class EmaTpsSmoother extends Module {
    private final NumberSetting windowPeriods = new NumberSetting("WindowPeriods", "Effective window periods N for exponential smoothing", 9.0, 3.0, 39.0, 2.0);
    private final BooleanSetting driftCompensation = new BooleanSetting("DriftCompensation", "Compensates for long-term server tick drift", true);
    private final BooleanSetting resetOnTeleport = new BooleanSetting("ResetOnTeleport", "Resets smoothing state upon dimension change or teleportation", true);

    public EmaTpsSmoother() {
        super("ema-tps-smoother", "EMA TPS Smoother", "Applies Exponential Moving Average (EMA) filtering (alpha = 2 / (N + 1)) to eliminate noisy micro-fluctuations in server TPS.", Category.NETWORK);
        addSettings(windowPeriods, driftCompensation, resetOnTeleport);
    }

    public NumberSetting getWindowPeriods() {
        return windowPeriods;
    }

    public BooleanSetting getDriftCompensation() {
        return driftCompensation;
    }

    public BooleanSetting getResetOnTeleport() {
        return resetOnTeleport;
    }
}
