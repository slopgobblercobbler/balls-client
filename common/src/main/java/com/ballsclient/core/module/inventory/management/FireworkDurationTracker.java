package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.NumberSetting;

import java.util.List;

/**
 * FireworkDurationTracker — Monitors elytra rocket inventory, flight duration tiers (1, 2, 3), and low-fuel alerts.
 */
public class FireworkDurationTracker extends Module {
    private final NumberSetting alertUnderRockets = new NumberSetting("AlertUnderRockets", "Low rocket inventory alert count threshold", 5.0, 1.0, 16.0, 1.0);

    public FireworkDurationTracker() {
        super("firework-duration-tracker", "Firework Duration Tracker", "Monitors elytra rocket inventory and low-fuel alerts", Category.INVENTORY);
        addSettings(alertUnderRockets);
    }

    public NumberSetting getAlertUnderRockets() {
        return alertUnderRockets;
    }

    public static int countFlightRockets(List<ItemSlot> inventory) {
        if (inventory == null) return 0;
        int count = 0;
        for (ItemSlot s : inventory) {
            if (s != null && !s.isEmpty() && s.itemId() != null && s.itemId().toLowerCase().contains("firework_rocket")) {
                count += s.count();
            }
        }
        return count;
    }
}
