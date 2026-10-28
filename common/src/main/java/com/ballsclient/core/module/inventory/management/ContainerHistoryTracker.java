package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ContainerHistoryTracker — Records recently opened storage containers with timestamps and location coordinates.
 */
public class ContainerHistoryTracker extends Module {
    private final NumberSetting historyCapacity = new NumberSetting("HistoryCapacity", "Maximum recorded container interactions", 20.0, 5.0, 100.0, 5.0);

    public ContainerHistoryTracker() {
        super("container-history-tracker", "Container History Tracker", "Records recently opened storage containers", Category.INVENTORY);
        addSettings(historyCapacity);
    }

    public NumberSetting getHistoryCapacity() {
        return historyCapacity;
    }
}
