package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * ValuablesMagneticPriority — Ranks floor items by material value (Netherite > Diamond > Iron) to collect valuables first.
 */
public class ValuablesMagneticPriority extends Module {
    private final BooleanSetting priorityNetherite = new BooleanSetting("PriorityNetherite", "Highest priority for Netherite items", true);
    private final BooleanSetting priorityDiamonds = new BooleanSetting("PriorityDiamonds", "High priority for Diamond items", true);

    public ValuablesMagneticPriority() {
        super("valuables-magnetic-priority", "Valuables Priority Magnet", "Ranks floor items by material value to collect valuables first", Category.INVENTORY);
        addSettings(priorityNetherite, priorityDiamonds);
    }

    public BooleanSetting getPriorityNetherite() {
        return priorityNetherite;
    }

    public BooleanSetting getPriorityDiamonds() {
        return priorityDiamonds;
    }

    public static int getItemValueScore(String itemId) {
        if (itemId == null) return 0;
        String id = itemId.toLowerCase();
        if (id.contains("netherite")) return 100;
        if (id.contains("ancient_debris")) return 90;
        if (id.contains("diamond")) return 80;
        if (id.contains("emerald")) return 60;
        if (id.contains("gold")) return 40;
        if (id.contains("iron")) return 20;
        return 1;
    }
}
