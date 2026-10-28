package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AirPocketSealer — Automatically detects and fills 1x1 holes in walls and floors within player reach.
 */
public class AirPocketSealer extends Module {
    private final NumberSetting searchRadius = new NumberSetting("SearchRadius", "Scanning radius for enclosed holes in blocks", 3.5, 2.0, 5.0, 0.5);

    public AirPocketSealer() {
        super("air-pocket-sealer", "Air Pocket Sealer", "Automatically detects and fills 1x1 holes in walls and floors within player reach", Category.AUTOMATION);
        addSettings(searchRadius);
    }

    public NumberSetting getSearchRadius() {
        return searchRadius;
    }

    public static boolean isSealedAirPocket(int adjacentSolidFaces) {
        return adjacentSolidFaces >= 4;
    }
}
