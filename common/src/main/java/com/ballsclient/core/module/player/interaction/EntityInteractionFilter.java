package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * EntityInteractionFilter — Filters accidental right clicks on villagers, armor stands, and item frames.
 */
public class EntityInteractionFilter extends Module {
    private final BooleanSetting blockVillagers = new BooleanSetting("BlockVillagers", "Suppress right-click interaction with villagers", true);
    private final BooleanSetting blockArmorStands = new BooleanSetting("BlockArmorStands", "Suppress right-click interaction with armor stands", true);
    private final BooleanSetting blockItemFrames = new BooleanSetting("BlockItemFrames", "Suppress right-click interaction with item frames", true);

    public EntityInteractionFilter() {
        super("entity-interaction-filter", "Entity Interact Filter", "Filters accidental right clicks on villagers, armor stands, and item frames", Category.PLAYER);
        addSettings(blockVillagers, blockArmorStands, blockItemFrames);
    }

    public BooleanSetting getBlockVillagers() {
        return blockVillagers;
    }

    public BooleanSetting getBlockArmorStands() {
        return blockArmorStands;
    }

    public BooleanSetting getBlockItemFrames() {
        return blockItemFrames;
    }

    public static boolean isInteractionSuppressed(String entityType, boolean blockV, boolean blockA, boolean blockI) {
        if (entityType == null) return false;
        String typeLower = entityType.toLowerCase();
        if (blockV && (typeLower.contains("villager") || typeLower.contains("wandering_trader"))) {
            return true;
        }
        if (blockA && typeLower.contains("armor_stand")) {
            return true;
        }
        if (blockI && (typeLower.contains("item_frame") || typeLower.contains("glow_item_frame"))) {
            return true;
        }
        return false;
    }
}
