package com.ballsclient.core.module.telemetry.packet;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * KeepAliveAlerter — Tracks server keep-alive challenge/response intervals and warns when heartbeats
 * are delayed near the timeout threshold.
 */
public class KeepAliveAlerter extends Module {
    private final NumberSetting warningTimeoutSeconds = new NumberSetting("WarningTimeoutSeconds", "Heartbeat delay warning threshold in seconds", 15.0, 5.0, 29.0, 1.0);
    private final BooleanSetting visualWarning = new BooleanSetting("VisualWarning", "Displays visual on-screen timeout warning", true);
    private final BooleanSetting soundChime = new BooleanSetting("SoundChime", "Plays alert chime on approaching timeout", false);

    public KeepAliveAlerter() {
        super("keep-alive-alerter", "Keep Alive Alerter", "Tracks server keep-alive challenge/response intervals and warns when heartbeats are delayed near the timeout threshold.", Category.NETWORK);
        addSettings(warningTimeoutSeconds, visualWarning, soundChime);
    }

    public NumberSetting getWarningTimeoutSeconds() {
        return warningTimeoutSeconds;
    }

    public BooleanSetting getVisualWarning() {
        return visualWarning;
    }

    public BooleanSetting getSoundChime() {
        return soundChime;
    }
}
