package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketCompressionRatio — Tracks Zlib/deflate packet compression efficiency, measuring uncompressed byte volume
 * versus wire bytes transmitted.
 */
public class PacketCompressionRatio extends Module {
    private final ModeSetting displayMode = new ModeSetting("DisplayMode", "Compression efficiency calculation display format", "RATIO_FACTOR", "RATIO_FACTOR", "SAVED_PERCENT", "TOTAL_SAVED_MB");
    private final BooleanSetting ignoreSmallPackets = new BooleanSetting("IgnoreSmallPackets", "Excludes uncompressed packets below compression threshold", true);
    private final NumberSetting resetIntervalSec = new NumberSetting("ResetIntervalSec", "Periodic ratio calculation reset interval in seconds", 30.0, 5.0, 120.0, 5.0);

    public PacketCompressionRatio() {
        super("packet-compression-ratio", "Packet Compression Ratio", "Tracks Zlib/deflate packet compression efficiency, measuring uncompressed byte volume versus wire bytes transmitted.", Category.NETWORK);
        addSettings(displayMode, ignoreSmallPackets, resetIntervalSec);
    }

    public ModeSetting getDisplayMode() {
        return displayMode;
    }

    public BooleanSetting getIgnoreSmallPackets() {
        return ignoreSmallPackets;
    }

    public NumberSetting getResetIntervalSec() {
        return resetIntervalSec;
    }
}
