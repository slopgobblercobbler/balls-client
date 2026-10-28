package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * EmergencyHotbarPuller — Pulls Totem of Undying or Ender Pearl into Hotbar Slot 1 when player health is critical.
 */
public class EmergencyHotbarPuller extends Module {
    private final NumberSetting healthThreshold = new NumberSetting("HealthThreshold", "Critical player health threshold in half-hearts", 6.0, 2.0, 12.0, 1.0);
    private final ModeSetting priorityItem = new ModeSetting("PriorityItem", "Emergency item type to pull", "Totem", "Totem", "EnderPearl");

    public EmergencyHotbarPuller() {
        super("emergency-hotbar-puller", "Emergency Hotbar Puller", "Pulls Totem or Pearl into Hotbar when player health is critical", Category.INVENTORY);
        addSettings(healthThreshold, priorityItem);
    }

    public NumberSetting getHealthThreshold() {
        return healthThreshold;
    }

    public ModeSetting getPriorityItem() {
        return priorityItem;
    }

    public static boolean isEmergency(double health, double threshold) {
        return health <= threshold;
    }
}
