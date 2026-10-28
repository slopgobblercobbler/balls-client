package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * UnexpectedPacketDetector — Detects and flags anomalous or illegal server-to-client packet combinations
 * that deviate from standard protocol handshake phases.
 */
public class UnexpectedPacketDetector extends Module {
    private final BooleanSetting strictProtocol = new BooleanSetting("StrictProtocol", "Enforces strict vanilla protocol state transitions", true);
    private final ModeSetting alertSensitivity = new ModeSetting("AlertSensitivity", "Sensitivity level for anomaly detection", "HIGH", "LOW", "MEDIUM", "HIGH");
    private final BooleanSetting logToConsole = new BooleanSetting("LogToConsole", "Logs unexpected packet occurrences to client console", true);

    public UnexpectedPacketDetector() {
        super("unexpected-packet-detector", "Unexpected Packet Detector", "Detects and flags anomalous or illegal server-to-client packet combinations that deviate from standard protocol handshake phases.", Category.NETWORK);
        addSettings(strictProtocol, alertSensitivity, logToConsole);
    }

    public BooleanSetting getStrictProtocol() {
        return strictProtocol;
    }

    public ModeSetting getAlertSensitivity() {
        return alertSensitivity;
    }

    public BooleanSetting getLogToConsole() {
        return logToConsole;
    }
}
