package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AutoDrop — Automatically purges designated junk items (diorite, granite, seeds) from inventory.
 */
public class AutoDrop extends Module {
    private final BooleanSetting dropCobblestone = new BooleanSetting("DropCobblestone", "Discard plain cobblestone stacks", true);
    private final BooleanSetting dropDirt = new BooleanSetting("DropDirt", "Discard plain dirt and gravel stacks", true);
    private final BooleanSetting dropNetherrack = new BooleanSetting("DropNetherrack", "Discard surplus netherrack blocks", false);
    private final NumberSetting dropIntervalTicks = new NumberSetting("DropIntervalTicks", "Ticks between consecutive drop packets", 1.0, 1.0, 20.0, 1.0);

    public AutoDrop() {
        super("auto-drop", "Auto Drop", "Automatically purges designated junk items (diorite, granite, seeds) from inventory", Category.PLAYER);
        addSettings(dropCobblestone, dropDirt, dropNetherrack, dropIntervalTicks);
    }

    public BooleanSetting getDropCobblestone() {
        return dropCobblestone;
    }

    public BooleanSetting getDropDirt() {
        return dropDirt;
    }

    public BooleanSetting getDropNetherrack() {
        return dropNetherrack;
    }

    public NumberSetting getDropIntervalTicks() {
        return dropIntervalTicks;
    }

    public static boolean isJunkItem(String itemId, boolean cobble, boolean dirt, boolean netherrack) {
        if (itemId == null) return false;
        String idLower = itemId.toLowerCase();
        if (cobble && idLower.contains("cobblestone")) return true;
        if (dirt && (idLower.contains("dirt") || idLower.contains("gravel") || idLower.contains("diorite") || idLower.contains("granite") || idLower.contains("andesite"))) return true;
        if (netherrack && idLower.contains("netherrack")) return true;
        return false;
    }
}
