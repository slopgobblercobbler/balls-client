package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Accelerates downward gravity after clearing obstacles to land faster.
 */
public class FastFall extends Module {
    private final NumberSetting gravityScale = new NumberSetting("GravityScale", "Gravity multiplication factor during descent", 2.5, 1.1, 6.0, 0.1);
    private final NumberSetting minFallHeight = new NumberSetting("MinFallHeight", "Fall distance in blocks before gravity boost begins", 1.5, 0.5, 5.0, 0.25);

    public FastFall() {
        super("fast-fall", "Fast Fall", "Accelerates downward gravity after clearing obstacles to land faster", Category.MOVEMENT);
        addSettings(gravityScale, minFallHeight);
    }

    public double getGravityScale() { return gravityScale.getValue(); }
    public double getMinFallHeight() { return minFallHeight.getValue(); }

    public double accelerateFall(double vy, double fallDist) {
        if (!isEnabled()) return vy;
        if (vy < 0.0 && fallDist >= getMinFallHeight()) {
            return vy - 0.08 * (getGravityScale() - 1.0);
        }
        return vy;
    }
}
