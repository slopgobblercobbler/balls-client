package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Anchor Refill — Restocks Glowstone blocks and Respawn Anchors to hotbar when stacks deplete
 */
public class AutoAnchorRefill extends Module {
    private final NumberSetting refillThreshold = new NumberSetting("RefillThreshold", "Stack size threshold to trigger restocking from inventory", 8.0, 1.0, 16.0, 1.0);
    private final BooleanSetting prioritizeGlowstone = new BooleanSetting("PrioritizeGlowstone", "Restock glowstone before anchors", true);

    public AutoAnchorRefill() {
        super("auto-anchor-refill", "Auto Anchor Refill", "Restocks Glowstone blocks and Respawn Anchors to hotbar when stacks deplete", Category.COMBAT);
        addSettings(refillThreshold, prioritizeGlowstone);
    }

    public double getRefillThreshold() {
        return refillThreshold.getValue();
    }

    public boolean isPrioritizeGlowstone() {
        return prioritizeGlowstone.getValue();
    }

    public boolean shouldRefillAnchorStack(int currentStackSize, int threshold) {
        return currentStackSize <= threshold;
    }
}
