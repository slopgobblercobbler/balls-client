package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BandwidthMeter — Real-time network throughput monitor measuring inbound (download)
 * and outbound (upload) data rates in KB/s.
 */
public class BandwidthMeter extends Module {
    private final ModeSetting unit = new ModeSetting("Unit", "Measurement bandwidth display unit", "KB_PER_SEC", "BYTES_PER_SEC", "KB_PER_SEC", "MB_PER_SEC", "BITS_PER_SEC");
    private final NumberSetting updatePeriodMs = new NumberSetting("UpdatePeriodMs", "Throughput recalculation interval in milliseconds", 500.0, 100.0, 2000.0, 100.0);
    private final BooleanSetting showUpload = new BooleanSetting("ShowUpload", "Displays outbound upload throughput alongside download", true);

    public BandwidthMeter() {
        super("bandwidth-meter", "Bandwidth Meter", "Real-time network throughput monitor measuring inbound (download) and outbound (upload) data rates in KB/s.", Category.NETWORK);
        addSettings(unit, updatePeriodMs, showUpload);
    }

    public ModeSetting getUnit() {
        return unit;
    }

    public NumberSetting getUpdatePeriodMs() {
        return updatePeriodMs;
    }

    public BooleanSetting getShowUpload() {
        return showUpload;
    }
}
