package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.List;

/**
 * GrindstoneDisenchantPreviewer — Computes returned experience points and durability bonuses upon disenchanting.
 */
public class GrindstoneDisenchantPreviewer extends Module {
    private final BooleanSetting includeDurabilityBonus = new BooleanSetting("IncludeDurabilityBonus", "Include 5% max durability repair bonus in calculation", true);

    public GrindstoneDisenchantPreviewer() {
        super("grindstone-disenchant-previewer", "Grindstone Previewer", "Computes returned experience points and durability bonuses", Category.INVENTORY);
        addSettings(includeDurabilityBonus);
    }

    public BooleanSetting getIncludeDurabilityBonus() {
        return includeDurabilityBonus;
    }

    public static int calculateDisenchantXP(List<Integer> enchantmentLevels) {
        if (enchantmentLevels == null || enchantmentLevels.isEmpty()) return 0;
        int xp = 0;
        for (int lvl : enchantmentLevels) {
            xp += (lvl * (lvl + 1)) / 2;
        }
        return xp;
    }

    public static int calculateCombinedDurability(int durA, int durB, int maxDur) {
        if (maxDur <= 0) return 0;
        int bonus = (int) Math.floor(maxDur * 0.05);
        return Math.min(maxDur, durA + durB + bonus);
    }
}
