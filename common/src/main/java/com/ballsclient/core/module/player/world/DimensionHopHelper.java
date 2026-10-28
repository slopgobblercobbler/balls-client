package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * DimensionHopHelper — Precomputes cross-dimensional transit displacement for End gateways and portals.
 */
public class DimensionHopHelper extends Module {
    private final ModeSetting targetDimension = new ModeSetting("TargetDimension", "Target destination dimension", "Overworld", "Overworld", "Nether", "TheEnd");
    private final BooleanSetting autoWaypointNether = new BooleanSetting("AutoWaypointNether", "Save automatic transit waypoint", true);
    private final NumberSetting gatewaySearchDistance = new NumberSetting("GatewaySearchDistance", "End gateway transit search radius", 1024.0, 128.0, 2048.0, 64.0);

    public DimensionHopHelper() {
        super("dimension-hop-helper", "Dimension Hop Helper", "Precomputes cross-dimensional transit displacement for End gateways and portals", Category.PLAYER);
        addSettings(targetDimension, autoWaypointNether, gatewaySearchDistance);
    }

    public ModeSetting getTargetDimension() {
        return targetDimension;
    }

    public BooleanSetting getAutoWaypointNether() {
        return autoWaypointNether;
    }

    public NumberSetting getGatewaySearchDistance() {
        return gatewaySearchDistance;
    }

    public static double[] getDimensionTransitOffset(double currentX, double currentZ, String fromDim, String toDim) {
        if ("Overworld".equalsIgnoreCase(fromDim) && "Nether".equalsIgnoreCase(toDim)) {
            return new double[]{currentX / 8.0, currentZ / 8.0};
        }
        if ("Nether".equalsIgnoreCase(fromDim) && "Overworld".equalsIgnoreCase(toDim)) {
            return new double[]{currentX * 8.0, currentZ * 8.0};
        }
        return new double[]{currentX, currentZ};
    }
}
