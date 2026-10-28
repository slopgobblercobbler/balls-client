package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Clamps downward vertical terminal velocity simulating Slow Falling.
 */
public class SlowFall extends Module {
    private final NumberSetting terminalVelocity = new NumberSetting("TerminalVelocity", "Terminal downward vertical speed clamp", -0.15, -0.8, -0.05, 0.01);
    private final BooleanSetting fastCancel = new BooleanSetting("FastCancel", "Temporarily cancel slow fall while holding sneak", false);

    public SlowFall() {
        super("slow-fall", "Slow Fall", "Clamps downward vertical terminal velocity simulating Slow Falling", Category.MOVEMENT);
        addSettings(terminalVelocity, fastCancel);
    }

    public double getTerminalVelocity() { return terminalVelocity.getValue(); }
    public boolean isFastCancel() { return fastCancel.getValue(); }

    public double clampFallVelocity(double vy) {
        if (!isEnabled()) return vy;
        return vy < getTerminalVelocity() ? getTerminalVelocity() : vy;
    }
}
