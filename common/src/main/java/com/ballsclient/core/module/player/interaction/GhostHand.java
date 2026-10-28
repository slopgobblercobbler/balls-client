package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * GhostHand — Interacts with containers, levers, and doors through non-solid barrier blocks.
 */
public class GhostHand extends Module {
    private final BooleanSetting interactChests = new BooleanSetting("InteractChests", "Allow interacting with buried chests", true);
    private final BooleanSetting interactLevers = new BooleanSetting("InteractLevers", "Allow toggling levers and buttons through walls", true);
    private final NumberSetting maxDepth = new NumberSetting("MaxDepth", "Maximum block wall penetration depth", 2.0, 1.0, 5.0, 1.0);

    public GhostHand() {
        super("ghost-hand", "Ghost Hand", "Interacts with containers, levers, and doors through non-solid barrier blocks", Category.PLAYER);
        addSettings(interactChests, interactLevers, maxDepth);
    }

    public BooleanSetting getInteractChests() {
        return interactChests;
    }

    public BooleanSetting getInteractLevers() {
        return interactLevers;
    }

    public NumberSetting getMaxDepth() {
        return maxDepth;
    }

    public static boolean isAllowedGhostTarget(String blockId, boolean allowChests, boolean allowLevers) {
        if (blockId == null) return false;
        String idLower = blockId.toLowerCase();
        if (allowChests && (idLower.contains("chest") || idLower.contains("shulker_box") || idLower.contains("barrel"))) {
            return true;
        }
        if (allowLevers && (idLower.contains("lever") || idLower.contains("button") || idLower.contains("door") || idLower.contains("trapdoor"))) {
            return true;
        }
        return false;
    }
}
