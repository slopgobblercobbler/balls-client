package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * FlowerPotPlanter — Places flowers, saplings, and cacti into empty decorative flower pots.
 */
public class FlowerPotPlanter extends Module {
    private final BooleanSetting autoSelectFlower = new BooleanSetting("AutoSelectFlower", "Pick optimal flower from inventory", true);
    private final BooleanSetting preventDuplicates = new BooleanSetting("PreventDuplicates", "Avoid planting identical flower types nearby", true);

    public FlowerPotPlanter() {
        super("flower-pot-planter", "Flower Pot Planter", "Places flowers, saplings, and cacti into empty decorative flower pots", Category.PLAYER);
        addSettings(autoSelectFlower, preventDuplicates);
    }

    public BooleanSetting getAutoSelectFlower() {
        return autoSelectFlower;
    }

    public BooleanSetting getPreventDuplicates() {
        return preventDuplicates;
    }

    public static boolean isPottablePlant(String itemId) {
        if (itemId == null) return false;
        String idLower = itemId.toLowerCase();
        return idLower.contains("tulip") || idLower.contains("orchid") || idLower.contains("dandelion") ||
                idLower.contains("poppy") || idLower.contains("rose") || idLower.contains("allium") ||
                idLower.contains("cornflower") || idLower.contains("cactus") || idLower.contains("sapling") ||
                idLower.contains("fungus") || idLower.contains("bamboo") || idLower.contains("azalea");
    }
}
