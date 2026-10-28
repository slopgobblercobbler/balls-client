package com.ballsclient.core.module.telemetry.tps;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ServerLagSpikeWarning — Monitors time gaps between server updates and triggers on-screen alerts
 * during major tick stalls or garbage collection pauses.
 */
public class ServerLagSpikeWarning extends Module {
    private final NumberSetting spikeThresholdMs = new NumberSetting("SpikeThresholdMs", "Server tick gap threshold triggering lag alert in milliseconds", 1000.0, 200.0, 5000.0, 100.0);
    private final BooleanSetting flashScreen = new BooleanSetting("FlashScreen", "Flashes subtle amber screen border during lag spike", false);
    private final BooleanSetting chatWarning = new BooleanSetting("ChatWarning", "Outputs chat notification with stall duration", true);

    public ServerLagSpikeWarning() {
        super("server-lag-spike-warning", "Server Lag Spike Warning", "Monitors time gaps between server updates and triggers on-screen alerts during major tick stalls or garbage collection pauses.", Category.NETWORK);
        addSettings(spikeThresholdMs, flashScreen, chatWarning);
    }

    public NumberSetting getSpikeThresholdMs() {
        return spikeThresholdMs;
    }

    public BooleanSetting getFlashScreen() {
        return flashScreen;
    }

    public BooleanSetting getChatWarning() {
        return chatWarning;
    }
}
