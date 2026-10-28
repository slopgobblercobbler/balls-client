package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Fast Bow — Releases bowstring at the exact tick full arrow velocity is achieved
 */
public class FastBow extends Module {
    private final NumberSetting releaseTick = new NumberSetting("ReleaseTick", "Bow draw ticks before auto-release", 20.0, 18.0, 22.0, 1.0);
    private final BooleanSetting autoRelease = new BooleanSetting("AutoRelease", "Automatically release use item packet", true);

    public FastBow() {
        super("fast-bow", "Fast Bow", "Releases bowstring at the exact tick full arrow velocity is achieved", Category.COMBAT);
        addSettings(releaseTick, autoRelease);
    }

    public double getReleaseTick() {
        return releaseTick.getValue();
    }

    public boolean isAutoRelease() {
        return autoRelease.getValue();
    }

    public double getArrowVelocityFromUseTicks(int useTicks) {
        float f = (float) useTicks / 20.0f;
        f = (f * f + f * 2.0f) / 3.0f;
        if (f > 1.0f) f = 1.0f;
        return f * 3.0;
    }
}
