package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * DumpNonGearJunk — One-click deposit/drop for non-equipment blocks and materials (dirt, cobblestone).
 */
public class DumpNonGearJunk extends Module {
    private final BooleanSetting keepFood = new BooleanSetting("KeepFood", "Do not dump food items", true);
    private final BooleanSetting keepTools = new BooleanSetting("KeepTools", "Do not dump tools or weapons", true);

    public DumpNonGearJunk() {
        super("dump-non-gear-junk", "Dump Non-Gear Junk", "One-click dump for non-equipment blocks and materials", Category.INVENTORY);
        addSettings(keepFood, keepTools);
    }

    public BooleanSetting getKeepFood() {
        return keepFood;
    }

    public BooleanSetting getKeepTools() {
        return keepTools;
    }

    public static boolean isJunkItem(ItemSlot slot, boolean keepFood, boolean keepTools) {
        if (slot == null || slot.isEmpty() || slot.itemId() == null) return false;
        String id = slot.itemId().toLowerCase();

        if (keepTools && (id.contains("sword") || id.contains("pickaxe") || id.contains("axe") || id.contains("shovel") || id.contains("hoe") || id.contains("bow") || id.contains("helmet") || id.contains("chestplate") || id.contains("leggings") || id.contains("boots") || id.contains("shield"))) {
            return false;
        }
        if (keepFood && (id.contains("apple") || id.contains("bread") || id.contains("beef") || id.contains("porkchop") || id.contains("mutton") || id.contains("chicken") || id.contains("carrot") || id.contains("potato") || id.contains("golden_carrot") || id.contains("potion"))) {
            return false;
        }

        return id.contains("cobblestone") || id.contains("dirt") || id.contains("gravel") || id.contains("sand")
                || id.contains("diorite") || id.contains("granite") || id.contains("andesite") || id.contains("tuff")
                || id.contains("deepslate") || id.contains("netherrack") || id.contains("rotten_flesh");
    }
}
