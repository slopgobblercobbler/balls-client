package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SculkCatalystFarmer — Tracks mob deaths within 8 blocks of Sculk Catalysts to optimize charge spread.
 */
public class SculkCatalystFarmer extends Module {
    private final NumberSetting catalystRadius = new NumberSetting("CatalystRadius", "Effective charge propagation radius around catalyst", 8.0, 2.0, 16.0, 1.0);
    private final BooleanSetting trackCharge = new BooleanSetting("TrackCharge", "Calculate cumulative experience charge stored", true);

    public SculkCatalystFarmer() {
        super("sculk-catalyst-farmer", "Sculk Catalyst Farmer", "Tracks mob deaths within 8 blocks of Sculk Catalysts to optimize charge spread", Category.PLAYER);
        addSettings(catalystRadius, trackCharge);
    }

    public NumberSetting getCatalystRadius() {
        return catalystRadius;
    }

    public BooleanSetting getTrackCharge() {
        return trackCharge;
    }

    public static boolean isWithinCatalystRange(double mobX, double mobY, double mobZ, double catX, double catY, double catZ, double radius) {
        double dx = mobX - catX;
        double dy = mobY - catY;
        double dz = mobZ - catZ;
        return Math.sqrt(dx * dx + dy * dy + dz * dz) <= radius;
    }
}
