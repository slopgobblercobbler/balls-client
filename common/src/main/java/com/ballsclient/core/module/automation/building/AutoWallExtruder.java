package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoWallExtruder — Extrudes vertical or horizontal block planes continuously along player movement direction.
 */
public class AutoWallExtruder extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final NumberSetting wallHeight = new NumberSetting("WallHeight", "Height of extruded vertical column in blocks", 3.0, 1.0, 8.0, 1.0);
    private final NumberSetting placementDelayTicks = new NumberSetting("PlacementDelayTicks", "Tick delay between block column placements", 1.0, 0.0, 5.0, 1.0);

    public AutoWallExtruder() {
        super("auto-wall-extruder", "Auto Wall Extruder", "Extrudes vertical or horizontal block planes continuously along player movement direction", Category.AUTOMATION);
        addSettings(wallHeight, placementDelayTicks);
    }

    public NumberSetting getWallHeight() {
        return wallHeight;
    }

    public NumberSetting getPlacementDelayTicks() {
        return placementDelayTicks;
    }

    public List<BlockPos> computeColumnPositions(int groundX, int groundY, int groundZ) {
        int h = (int) Math.round(wallHeight.getValue());
        List<BlockPos> list = new ArrayList<>(h);
        for (int i = 0; i < h; i++) {
            list.add(new BlockPos(groundX, groundY + i, groundZ));
        }
        return list;
    }
}
