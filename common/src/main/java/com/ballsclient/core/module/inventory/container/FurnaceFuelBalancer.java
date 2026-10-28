package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * FurnaceFuelBalancer — Calculates exact fuel counts (coal, blaze rods, lava) needed for input smelt items.
 */
public class FurnaceFuelBalancer extends Module {
    private final ModeSetting preferredFuel = new ModeSetting("PreferredFuel", "Default furnace fuel item", "Coal", "Coal", "BlazeRod", "LavaBucket", "Planks");

    public FurnaceFuelBalancer() {
        super("furnace-fuel-balancer", "Furnace Fuel Balancer", "Calculates exact fuel counts needed for input smelt items", Category.INVENTORY);
        addSettings(preferredFuel);
    }

    public ModeSetting getPreferredFuel() {
        return preferredFuel;
    }

    public static int calculateRequiredFuel(int itemCount, int itemBurnTimeYield) {
        if (itemBurnTimeYield <= 0) return 0;
        return (int) Math.ceil(itemCount / (double) itemBurnTimeYield);
    }
}
