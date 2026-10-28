package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * PickupDelayEliminator — Monitors 40-tick pickup delay on newly dropped items and targets 0-delay items.
 */
public class PickupDelayEliminator extends Module {
    private final BooleanSetting instantPickCandidateOnly = new BooleanSetting("InstantPickCandidateOnly", "Only attempt collecting items with 0 pickup delay", true);

    public PickupDelayEliminator() {
        super("pickup-delay-eliminator", "Pickup Delay Eliminator", "Monitors pickup delay and targets collectible items", Category.INVENTORY);
        addSettings(instantPickCandidateOnly);
    }

    public BooleanSetting getInstantPickCandidateOnly() {
        return instantPickCandidateOnly;
    }

    public static boolean canBePickedUp(int pickupDelayTicks) {
        return pickupDelayTicks <= 0;
    }
}
