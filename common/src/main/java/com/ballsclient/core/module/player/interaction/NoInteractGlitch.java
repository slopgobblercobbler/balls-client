package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * NoInteractGlitch — Suppresses client ghost block desync via server acknowledgement tracking.
 */
public class NoInteractGlitch extends Module {
    private final BooleanSetting verifyAck = new BooleanSetting("VerifyAck", "Wait for server block change acknowledgement packet", true);
    private final NumberSetting ackTimeoutMs = new NumberSetting("AckTimeoutMs", "Acknowledgement latency timeout limit", 250.0, 50.0, 1000.0, 25.0);
    private final BooleanSetting logResync = new BooleanSetting("LogResync", "Log desynchronization correction events to console", false);

    public NoInteractGlitch() {
        super("no-interact-glitch", "No Interact Glitch", "Suppresses client ghost block desync via server acknowledgement tracking", Category.PLAYER);
        addSettings(verifyAck, ackTimeoutMs, logResync);
    }

    public BooleanSetting getVerifyAck() {
        return verifyAck;
    }

    public NumberSetting getAckTimeoutMs() {
        return ackTimeoutMs;
    }

    public BooleanSetting getLogResync() {
        return logResync;
    }

    public static boolean isDesynced(long sendTime, long ackTime, double timeoutMs) {
        if (ackTime < sendTime) {
            long elapsed = System.currentTimeMillis() - sendTime;
            return elapsed > timeoutMs;
        }
        return false;
    }
}
