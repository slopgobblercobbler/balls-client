package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.module.inventory.SlotSwap;
import com.ballsclient.core.setting.ModeSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CustomSlotTemplateSort — Organizes inventory layout matching custom user-defined archetype schema templates.
 */
public class CustomSlotTemplateSort extends Module {
    private final ModeSetting templateName = new ModeSetting("TemplateName", "Active archetype template layout schema", "DefaultPvP", "DefaultPvP", "Builder", "Miner");

    public CustomSlotTemplateSort() {
        super("custom-slot-template-sort", "Custom Template Sort", "Organizes inventory layout matching custom user-defined archetype schema templates", Category.INVENTORY);
        addSettings(templateName);
    }

    public ModeSetting getTemplateName() {
        return templateName;
    }

    public static List<SlotSwap> applyTemplate(List<ItemSlot> inventory, Map<Integer, String> templateSlotToItemId) {
        List<SlotSwap> swaps = new ArrayList<>();
        if (inventory == null || templateSlotToItemId == null) return swaps;

        for (Map.Entry<Integer, String> entry : templateSlotToItemId.entrySet()) {
            int targetSlot = entry.getKey();
            String desiredItem = entry.getValue();

            if (targetSlot < inventory.size()) {
                ItemSlot current = inventory.get(targetSlot);
                if (current != null && current.itemId() != null && current.itemId().equalsIgnoreCase(desiredItem)) {
                    continue;
                }
            }

            for (int i = 0; i < inventory.size(); i++) {
                if (i == targetSlot) continue;
                ItemSlot cand = inventory.get(i);
                if (cand != null && cand.itemId() != null && cand.itemId().equalsIgnoreCase(desiredItem)) {
                    swaps.add(new SlotSwap(i, targetSlot));
                    break;
                }
            }
        }
        return swaps;
    }
}
