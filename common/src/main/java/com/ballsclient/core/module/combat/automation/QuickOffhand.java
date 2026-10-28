package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Quick Offhand — Instant hotkey swapping of selected combat items directly into offhand
 */
public class QuickOffhand extends Module {
    private final ModeSetting targetItem = new ModeSetting("TargetItem", "Target combat item to cycle into offhand", "Totem", "Totem", "GoldenApple", "Shield", "Crystal", "WindCharge");

    public QuickOffhand() {
        super("quick-offhand", "Quick Offhand", "Instant hotkey swapping of selected combat items directly into offhand", Category.COMBAT);
        addSettings(targetItem);
    }

    public String getTargetItem() {
        return targetItem.getValue();
    }

    public boolean isItemInOffhand(String currentOffhandItem, String targetItem) {
        return targetItem.equalsIgnoreCase(currentOffhandItem);
    }
}
