package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ReachExpander — Expands block placement and interaction reach up to 6.0 meters.
 */
public class ReachExpander extends Module {
    private final NumberSetting blockReach = new NumberSetting("BlockReach", "Block interaction reach distance", 5.0, 4.5, 6.0, 0.1);
    private final NumberSetting entityReach = new NumberSetting("EntityReach", "Entity interaction reach distance", 4.5, 3.0, 6.0, 0.1);
    private final BooleanSetting creativeBypass = new BooleanSetting("CreativeBypass", "Respect vanilla creative reach limit", false);

    public ReachExpander() {
        super("reach-expander", "Reach Expander", "Expands block placement and interaction reach up to 6.0 meters", Category.PLAYER);
        addSettings(blockReach, entityReach, creativeBypass);
    }

    public NumberSetting getBlockReach() {
        return blockReach;
    }

    public NumberSetting getEntityReach() {
        return entityReach;
    }

    public BooleanSetting getCreativeBypass() {
        return creativeBypass;
    }

    public static boolean isTargetWithinReach(double distance, double customReach) {
        return distance <= customReach;
    }
}
