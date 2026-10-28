package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Detects falls into the void and bounces/teleports player to safety.
 */
public class AntiVoid extends Module {
    private final NumberSetting voidLevel = new NumberSetting("VoidLevel", "Y-elevation threshold triggering anti-void rescue", 0.0, -64.0, 32.0, 1.0);
    private final ModeSetting mode = new ModeSetting("Mode", "Rescue mechanism", "Bounce", "Bounce", "PacketFreeze", "SafeSurface");
    private final NumberSetting autoTriggerDistance = new NumberSetting("AutoTriggerDistance", "Maximum distance below void level before hard bounce", 8.0, 2.0, 20.0, 1.0);

    public AntiVoid() {
        super("anti-void", "Anti Void", "Detects falls into the void and bounces/teleports player to safety", Category.MOVEMENT);
        addSettings(voidLevel, mode, autoTriggerDistance);
    }

    public double getVoidLevel() { return voidLevel.getValue(); }
    public String getMode() { return mode.getValue(); }
    public double getAutoTriggerDistance() { return autoTriggerDistance.getValue(); }

    public boolean shouldTriggerAntiVoid(double y, double vy, boolean blocksBelow) {
        return isEnabled() && y < getVoidLevel() && vy < 0.0 && !blocksBelow;
    }

    public double getBounceImpulse() {
        return 1.2;
    }
}
