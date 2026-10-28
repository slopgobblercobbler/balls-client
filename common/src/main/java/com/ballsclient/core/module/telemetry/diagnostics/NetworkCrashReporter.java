package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * NetworkCrashReporter — Catches Netty decoder exceptions, unhandled packet exceptions,
 * and pipeline crashes to provide actionable diagnostic reports.
 */
public class NetworkCrashReporter extends Module {
    private final BooleanSetting captureFullStack = new BooleanSetting("CaptureFullStack", "Captures entire Java exception stack trace in report", true);
    private final BooleanSetting dumpOffendingPacket = new BooleanSetting("DumpOffendingPacket", "Dumps raw bytes of packet causing pipeline crash", true);
    private final BooleanSetting silentRecovery = new BooleanSetting("SilentRecovery", "Attempts pipeline recovery without disconnecting if non-fatal", false);

    public NetworkCrashReporter() {
        super("network-crash-reporter", "Network Crash Reporter", "Catches Netty decoder exceptions, unhandled packet exceptions, and pipeline crashes to provide actionable diagnostic reports.", Category.NETWORK);
        addSettings(captureFullStack, dumpOffendingPacket, silentRecovery);
    }

    public BooleanSetting getCaptureFullStack() {
        return captureFullStack;
    }

    public BooleanSetting getDumpOffendingPacket() {
        return dumpOffendingPacket;
    }

    public BooleanSetting getSilentRecovery() {
        return silentRecovery;
    }
}
