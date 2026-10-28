package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PacketLogger — Intercepts, records, and formats inbound and outbound network packets
 * with timestamp, class name, and payload size.
 */
public class PacketLogger extends Module {
    private final ModeSetting direction = new ModeSetting("Direction", "Packet stream direction to log", "BOTH", "INBOUND", "OUTBOUND", "BOTH");
    private final NumberSetting maxHistory = new NumberSetting("MaxHistory", "Maximum packet history entries to retain", 200.0, 50.0, 1000.0, 25.0);
    private final BooleanSetting logPayloadHex = new BooleanSetting("LogPayloadHex", "Include hexadecimal payload dump", false);

    public PacketLogger() {
        super("packet-logger", "Packet Logger", "Intercepts, records, and formats inbound and outbound network packets with timestamp, class name, and payload size.", Category.NETWORK);
        addSettings(direction, maxHistory, logPayloadHex);
    }

    public ModeSetting getDirection() {
        return direction;
    }

    public NumberSetting getMaxHistory() {
        return maxHistory;
    }

    public BooleanSetting getLogPayloadHex() {
        return logPayloadHex;
    }
}
