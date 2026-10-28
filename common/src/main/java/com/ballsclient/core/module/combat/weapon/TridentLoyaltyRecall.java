package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Trident Loyalty Recall — Monitors Loyalty III return vector and indicates ready-to-throw state
 */
public class TridentLoyaltyRecall extends Module {
    private final BooleanSetting autoSelectOnReturn = new BooleanSetting("AutoSelectOnReturn", "Auto-select hotbar slot when trident lands in inventory", true);
    private final BooleanSetting alertOnReturn = new BooleanSetting("AlertOnReturn", "Audio tone when trident returns to hand", true);

    public TridentLoyaltyRecall() {
        super("trident-loyalty-recall", "Trident Loyalty Recall", "Monitors Loyalty III return vector and indicates ready-to-throw state", Category.COMBAT);
        addSettings(autoSelectOnReturn, alertOnReturn);
    }

    public boolean isAutoSelectOnReturn() {
        return autoSelectOnReturn.getValue();
    }

    public boolean isAlertOnReturn() {
        return alertOnReturn.getValue();
    }

    public double calculateLoyaltyReturnSpeed(int loyaltyLevel) {
        return 0.5 * loyaltyLevel + 0.5;
    }
}
