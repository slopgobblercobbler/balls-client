package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoFish — Bobber vertical velocity and splash sound physics monitor for instant reeling.
 */
public class AutoFish extends Module {
    private final NumberSetting catchVelocityThreshold = new NumberSetting("CatchVelocityThreshold", "Bobber downward velocity to trigger reel", -0.05, -0.2, -0.01, 0.01);
    private final NumberSetting recastDelayMs = new NumberSetting("RecastDelayMs", "Delay before recasting fishing rod", 350.0, 100.0, 1000.0, 25.0);
    private final BooleanSetting checkSplashSound = new BooleanSetting("CheckSplashSound", "Confirm bite using splash sound audio event", true);

    public AutoFish() {
        super("auto-fish", "Auto Fish", "Bobber vertical velocity and splash sound physics monitor for instant reeling", Category.PLAYER);
        addSettings(catchVelocityThreshold, recastDelayMs, checkSplashSound);
    }

    public NumberSetting getCatchVelocityThreshold() {
        return catchVelocityThreshold;
    }

    public NumberSetting getRecastDelayMs() {
        return recastDelayMs;
    }

    public BooleanSetting getCheckSplashSound() {
        return checkSplashSound;
    }

    public static boolean isBiteTriggered(double bobberVy, boolean splashEvent, double threshold) {
        return bobberVy < threshold || splashEvent;
    }
}
