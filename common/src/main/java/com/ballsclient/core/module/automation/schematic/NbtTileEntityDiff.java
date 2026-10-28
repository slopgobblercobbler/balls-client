package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Map;
import java.util.Objects;

/**
 * NbtTileEntityDiff — Compares chest contents, sign text, and command block NBT in schematic against placed blocks.
 */
public class NbtTileEntityDiff extends Module {
    private final BooleanSetting checkContainers = new BooleanSetting("CheckContainers", "Compares container item inventories", true);
    private final BooleanSetting checkSigns = new BooleanSetting("CheckSigns", "Compares sign text lines", true);

    public NbtTileEntityDiff() {
        super("nbt-tile-entity-diff", "NBT Tile Entity Diff", "Compares chest contents, sign text, and command block NBT in schematic against placed blocks", Category.AUTOMATION);
        addSettings(checkContainers, checkSigns);
    }

    public BooleanSetting getCheckContainers() {
        return checkContainers;
    }

    public BooleanSetting getCheckSigns() {
        return checkSigns;
    }

    public static boolean areNbtTagsEqual(Map<String, Object> schemTag, Map<String, Object> worldTag) {
        if (schemTag == null && worldTag == null) return true;
        if (schemTag == null || worldTag == null) return false;
        return Objects.equals(schemTag, worldTag);
    }
}
