package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoSmelter — Distributes ores and fuels across furnaces to maintain continuous smelting.
 */
public class AutoSmelter extends Module {
    private final ModeSetting fuelPreference = new ModeSetting("FuelPreference", "Preferred fuel source", "Coal", "Coal", "LavaBucket", "BlazeRod", "Charcoal");
    private final NumberSetting minOreBatch = new NumberSetting("MinOreBatch", "Minimum ore count before loading furnace", 8.0, 1.0, 64.0, 1.0);

    public AutoSmelter() {
        super("auto-smelter", "Auto Smelter", "Distributes ores and fuels across furnaces to maintain continuous smelting", Category.PLAYER);
        addSettings(fuelPreference, minOreBatch);
    }

    public ModeSetting getFuelPreference() {
        return fuelPreference;
    }

    public NumberSetting getMinOreBatch() {
        return minOreBatch;
    }

    public static int getFuelCountNeeded(int itemCount, int itemsPerFuelUnit) {
        if (itemsPerFuelUnit <= 0) return 0;
        return (int) Math.ceil((double) itemCount / itemsPerFuelUnit);
    }
}
