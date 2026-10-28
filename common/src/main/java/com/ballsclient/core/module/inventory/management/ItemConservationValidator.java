package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemConservationValidator — Audits inventory before/after transfers to verify total item count invariant strictly holds.
 */
public class ItemConservationValidator extends Module {
    private final BooleanSetting strictMode = new BooleanSetting("StrictMode", "Enforce exact multiset identity across all transfers", true);
    private final BooleanSetting logViolations = new BooleanSetting("LogViolations", "Log checksum diff anomalies to console", true);

    public ItemConservationValidator() {
        super("item-conservation-validator", "Item Conservation Validator", "Audits inventory before/after transfers to verify item conservation", Category.INVENTORY);
        addSettings(strictMode, logViolations);
    }

    public BooleanSetting getStrictMode() {
        return strictMode;
    }

    public BooleanSetting getLogViolations() {
        return logViolations;
    }

    public static Map<String, Integer> calculateItemTotals(List<ItemSlot> slots) {
        Map<String, Integer> totals = new HashMap<>();
        if (slots == null) return totals;
        for (ItemSlot s : slots) {
            if (s != null && !s.isEmpty() && s.itemId() != null && s.count() > 0) {
                totals.put(s.itemId(), totals.getOrDefault(s.itemId(), 0) + s.count());
            }
        }
        return totals;
    }

    public static boolean verifyConservation(List<ItemSlot> before, List<ItemSlot> after) {
        Map<String, Integer> totalsBefore = calculateItemTotals(before);
        Map<String, Integer> totalsAfter = calculateItemTotals(after);
        return totalsBefore.equals(totalsAfter);
    }
}
