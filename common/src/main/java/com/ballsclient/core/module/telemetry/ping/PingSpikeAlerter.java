package com.ballsclient.core.module.telemetry.ping;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PingSpikeAlerter — Detects sudden latency surges exceeding baseline thresholds and triggers
 * visual or auditory warnings before PvP encounters.
 */
public class PingSpikeAlerter extends Module {
    private final NumberSetting spikeDeltaMs = new NumberSetting("SpikeDeltaMs", "Minimum latency increase triggering a spike alert", 50.0, 15.0, 200.0, 5.0);
    private final NumberSetting cooldownSeconds = new NumberSetting("CooldownSeconds", "Cooldown interval between consecutive spike alerts", 5.0, 1.0, 30.0, 1.0);
    private final BooleanSetting audioAlert = new BooleanSetting("AudioAlert", "Plays alert tone when ping spike is detected", false);

    public PingSpikeAlerter() {
        super("ping-spike-alerter", "Ping Spike Alerter", "Detects sudden latency surges exceeding baseline thresholds and triggers visual or auditory warnings before PvP encounters.", Category.NETWORK);
        addSettings(spikeDeltaMs, cooldownSeconds, audioAlert);
    }

    public NumberSetting getSpikeDeltaMs() {
        return spikeDeltaMs;
    }

    public NumberSetting getCooldownSeconds() {
        return cooldownSeconds;
    }

    public BooleanSetting getAudioAlert() {
        return audioAlert;
    }
}
