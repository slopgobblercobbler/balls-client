package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketDumper — Serializes raw packet byte streams to local disk files for deep protocol inspection,
 * debugging, and reverse engineering.
 */
public class PacketDumper extends Module {
    private final ModeSetting format = new ModeSetting("Format", "Output dump serialization format", "JSON", "JSON", "RAW_HEX", "PCAP_COMPAT");
    private final NumberSetting bufferSizeKb = new NumberSetting("BufferSizeKb", "Disk write buffer size in kilobytes", 64.0, 16.0, 512.0, 16.0);
    private final BooleanSetting autoFlush = new BooleanSetting("AutoFlush", "Immediately flushes packets to disk on arrival", false);

    public PacketDumper() {
        super("packet-dumper", "Packet Dumper", "Serializes raw packet byte streams to local disk files for deep protocol inspection, debugging, and reverse engineering.", Category.NETWORK);
        addSettings(format, bufferSizeKb, autoFlush);
    }

    public ModeSetting getFormat() {
        return format;
    }

    public NumberSetting getBufferSizeKb() {
        return bufferSizeKb;
    }

    public BooleanSetting getAutoFlush() {
        return autoFlush;
    }
}
