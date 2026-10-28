package com.ballsclient.core.module.telemetry.diagnostics;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PortalGodModeHelper — Monitors nether portal transition packets and client loading states
 * to track invulnerability windows during dimension transitions.
 */
public class PortalGodModeHelper extends Module {
    private final BooleanSetting countdownTimer = new BooleanSetting("CountdownTimer", "Displays remaining portal invulnerability countdown on HUD", true);
    private final NumberSetting invulnerabilityDurationSec = new NumberSetting("InvulnerabilityDurationSec", "Expected portal immunity window in seconds", 3.0, 1.0, 10.0, 0.5);
    private final BooleanSetting alerterSound = new BooleanSetting("AlerterSound", "Plays sound chime when portal invulnerability is expiring", false);

    public PortalGodModeHelper() {
        super("portal-god-mode-helper", "Portal God Mode Helper", "Monitors nether portal transition packets and client loading states to track invulnerability windows during dimension transitions.", Category.NETWORK);
        addSettings(countdownTimer, invulnerabilityDurationSec, alerterSound);
    }

    public BooleanSetting getCountdownTimer() {
        return countdownTimer;
    }

    public NumberSetting getInvulnerabilityDurationSec() {
        return invulnerabilityDurationSec;
    }

    public BooleanSetting getAlerterSound() {
        return alerterSound;
    }
}
