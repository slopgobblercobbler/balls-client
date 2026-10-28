package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * PickaxeSwitchGlitchFix — Syncs client tool swapping with server break progress to prevent block resets.
 */
public class PickaxeSwitchGlitchFix extends Module {
    private final BooleanSetting waitForAck = new BooleanSetting("WaitForAck", "Wait for server block destruction packet before switching tool", true);
    private final NumberSetting latencyBufferMs = new NumberSetting("LatencyBufferMs", "Network RTT latency compensation buffer", 50.0, 10.0, 200.0, 10.0);

    public PickaxeSwitchGlitchFix() {
        super("pickaxe-switch-glitch-fix", "Pickaxe Glitch Fix", "Syncs client tool swapping with server break progress to prevent block resets", Category.PLAYER);
        addSettings(waitForAck, latencyBufferMs);
    }

    public BooleanSetting getWaitForAck() {
        return waitForAck;
    }

    public NumberSetting getLatencyBufferMs() {
        return latencyBufferMs;
    }

    public static boolean canSwitchSlot(float breakProgress, long lastPacketTime, long pingMs, double latencyBuffer) {
        if (breakProgress < 1.0f) {
            return false;
        }
        long elapsed = System.currentTimeMillis() - lastPacketTime;
        return elapsed >= (pingMs + latencyBuffer);
    }
}
