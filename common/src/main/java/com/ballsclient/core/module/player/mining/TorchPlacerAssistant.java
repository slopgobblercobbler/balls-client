package com.ballsclient.core.module.player.mining;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TorchPlacerAssistant — Automatically places torches when ambient block light drops below threshold.
 */
public class TorchPlacerAssistant extends Module {
    private final NumberSetting minLightLevel = new NumberSetting("MinLightLevel", "Minimum ambient light to trigger placement", 6.0, 0.0, 10.0, 1.0);
    private final NumberSetting torchSpacing = new NumberSetting("TorchSpacing", "Minimum distance between consecutive torches", 8.0, 4.0, 16.0, 1.0);
    private final BooleanSetting placeOnFloorOnly = new BooleanSetting("PlaceOnFloorOnly", "Restrict torch placement to solid floor faces", true);

    public TorchPlacerAssistant() {
        super("torch-placer-assistant", "Torch Placer Assistant", "Automatically places torches when ambient block light drops below threshold", Category.PLAYER);
        addSettings(minLightLevel, torchSpacing, placeOnFloorOnly);
    }

    public NumberSetting getMinLightLevel() {
        return minLightLevel;
    }

    public NumberSetting getTorchSpacing() {
        return torchSpacing;
    }

    public BooleanSetting getPlaceOnFloorOnly() {
        return placeOnFloorOnly;
    }

    public static boolean shouldPlaceTorch(int lightLevel, double distanceTraveled, double minLight, double spacing) {
        return lightLevel <= minLight && distanceTraveled >= spacing;
    }
}
