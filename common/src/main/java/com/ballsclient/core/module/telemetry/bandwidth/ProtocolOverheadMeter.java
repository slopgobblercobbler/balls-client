package com.ballsclient.core.module.telemetry.bandwidth;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * ProtocolOverheadMeter — Calculates the percentage of network traffic consumed by protocol framing,
 * packet headers, VarInt lengths, and encryption.
 */
public class ProtocolOverheadMeter extends Module {
    private final BooleanSetting includeEncryption = new BooleanSetting("IncludeEncryption", "Includes AES/CFB8 transport encryption overhead in calculation", true);
    private final BooleanSetting includeVarIntFraming = new BooleanSetting("IncludeVarIntFraming", "Accounts for VarInt packet length and ID framing headers", true);
    private final ModeSetting displayFormat = new ModeSetting("DisplayFormat", "Protocol overhead calculation presentation format", "PERCENTAGE", "PERCENTAGE", "OVERHEAD_BYTES", "RATIO");

    public ProtocolOverheadMeter() {
        super("protocol-overhead-meter", "Protocol Overhead Meter", "Calculates the percentage of network traffic consumed by protocol framing, packet headers, VarInt lengths, and encryption.", Category.NETWORK);
        addSettings(includeEncryption, includeVarIntFraming, displayFormat);
    }

    public BooleanSetting getIncludeEncryption() {
        return includeEncryption;
    }

    public BooleanSetting getIncludeVarIntFraming() {
        return includeVarIntFraming;
    }

    public ModeSetting getDisplayFormat() {
        return displayFormat;
    }
}
