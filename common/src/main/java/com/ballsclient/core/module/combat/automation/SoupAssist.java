package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Soup Assist — Consumes mushroom or rabbit stew instantly in KitPvP and drops or moves empty bowls
 */
public class SoupAssist extends Module {
    private final NumberSetting healthTrigger = new NumberSetting("HealthTrigger", "Health threshold to consume stew", 14.0, 8.0, 16.0, 0.5);
    private final BooleanSetting dropEmptyBowl = new BooleanSetting("DropEmptyBowl", "Automatically drop bowl with drop packet", true);

    public SoupAssist() {
        super("soup-assist", "Soup Assist", "Consumes mushroom or rabbit stew instantly in KitPvP and drops or moves empty bowls", Category.COMBAT);
        addSettings(healthTrigger, dropEmptyBowl);
    }

    public double getHealthTrigger() {
        return healthTrigger.getValue();
    }

    public boolean isDropEmptyBowl() {
        return dropEmptyBowl.getValue();
    }

    public boolean shouldEatSoup(double currentHealth, double triggerHp, int soupSlot) {
        return currentHealth <= triggerHp && soupSlot >= 0;
    }
}
