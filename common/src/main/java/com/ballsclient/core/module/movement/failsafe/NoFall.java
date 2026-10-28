package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Mitigates fall damage via packet ground-spoofing or water landing.
 */
public class NoFall extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Fall damage mitigation strategy", "Packet", "Packet", "Bucket", "Catch", "DamageCancel");
    private final NumberSetting fallThreshold = new NumberSetting("FallThreshold", "Fall distance in blocks before triggering mitigation", 3.0, 1.0, 10.0, 0.5);
    private final BooleanSetting silent = new BooleanSetting("Silent", "Suppress spoofing packets in client telemetry", true);

    public NoFall() {
        super("no-fall", "No Fall", "Mitigates fall damage via packet ground-spoofing or water landing", Category.MOVEMENT);
        addSettings(mode, fallThreshold, silent);
    }

    public String getMode() { return mode.getValue(); }
    public double getFallThreshold() { return fallThreshold.getValue(); }
    public boolean isSilent() { return silent.getValue(); }

    public boolean shouldSpoofGround(double fallDistance) {
        return isEnabled() && fallDistance >= getFallThreshold();
    }

    public int calculateMitigatedDamage(double fallDistance) {
        if (!isEnabled()) return (int) Math.max(0, Math.floor(fallDistance - 3.0));
        if (shouldSpoofGround(fallDistance)) return 0;
        return (int) Math.max(0, Math.floor(fallDistance - 3.0));
    }
}
