package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoBreed — Identifies breedable animals within reach and feeds them until love mode triggers.
 */
public class AutoBreed extends Module {
    private final NumberSetting cooldownMs = new NumberSetting("CooldownMs", "Interaction cooldown delay between entities", 500.0, 100.0, 2000.0, 50.0);
    private final NumberSetting maxPairs = new NumberSetting("MaxPairs", "Maximum animal pairs to breed per cycle", 10.0, 1.0, 30.0, 1.0);
    private final BooleanSetting checkBaby = new BooleanSetting("CheckBaby", "Ignore baby animals unable to breed", true);

    public AutoBreed() {
        super("auto-breed", "Auto Breed", "Identifies breedable animals within reach and feeds them until love mode triggers", Category.PLAYER);
        addSettings(cooldownMs, maxPairs, checkBaby);
    }

    public NumberSetting getCooldownMs() {
        return cooldownMs;
    }

    public NumberSetting getMaxPairs() {
        return maxPairs;
    }

    public BooleanSetting getCheckBaby() {
        return checkBaby;
    }

    public static boolean canBreed(boolean isAdult, boolean inLove, long timeSinceLastBreed, double cooldownMs) {
        return isAdult && !inLove && timeSinceLastBreed >= cooldownMs;
    }
}
