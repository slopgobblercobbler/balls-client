package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * DurabilitySaver — Halts mining or swaps tools when remaining durability drops below safety floor.
 */
public class DurabilitySaver extends Module {
    private final NumberSetting safetyThresholdHits = new NumberSetting("SafetyThresholdHits", "Minimum durability remaining before locking tool", 5.0, 1.0, 20.0, 1.0);
    private final BooleanSetting alertInChat = new BooleanSetting("AlertInChat", "Send chat warning when tool reaches threshold", true);
    private final BooleanSetting lockSlot = new BooleanSetting("LockSlot", "Prevent packets using tool in current hotbar slot", true);

    public DurabilitySaver() {
        super("durability-saver", "Durability Saver", "Halts mining or swaps tools when remaining durability drops below safety floor", Category.PLAYER);
        addSettings(safetyThresholdHits, alertInChat, lockSlot);
    }

    public NumberSetting getSafetyThresholdHits() {
        return safetyThresholdHits;
    }

    public BooleanSetting getAlertInChat() {
        return alertInChat;
    }

    public BooleanSetting getLockSlot() {
        return lockSlot;
    }

    public static boolean isDurabilityExhausted(int currentDamage, int maxDamage, double thresholdHits) {
        int remainingDurability = maxDamage - currentDamage;
        return remainingDurability <= thresholdHits;
    }
}
