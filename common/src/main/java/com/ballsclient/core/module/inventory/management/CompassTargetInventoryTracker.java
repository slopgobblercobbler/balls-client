package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * CompassTargetInventoryTracker — Tracks lodestone compasses and recovery compass targets from inventory slots.
 */
public class CompassTargetInventoryTracker extends Module {
    private final BooleanSetting trackLodestones = new BooleanSetting("TrackLodestones", "Track lodestone compass destination coordinates", true);

    public CompassTargetInventoryTracker() {
        super("compass-target-inventory-tracker", "Compass Target Inventory Tracker", "Tracks lodestone compass targets from inventory slots", Category.INVENTORY);
        addSettings(trackLodestones);
    }

    public BooleanSetting getTrackLodestones() {
        return trackLodestones;
    }
}
