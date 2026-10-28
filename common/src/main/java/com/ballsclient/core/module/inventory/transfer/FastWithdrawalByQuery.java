package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FastWithdrawalByQuery — Pulls all container items matching a search filter into player inventory.
 */
public class FastWithdrawalByQuery extends Module {
    private final BooleanSetting ignoreCase = new BooleanSetting("IgnoreCase", "Case-insensitive query search", true);

    public FastWithdrawalByQuery() {
        super("fast-withdrawal-by-query", "Fast Withdrawal By Query", "Pulls container items matching search filter into inventory", Category.INVENTORY);
        addSettings(ignoreCase);
    }

    public BooleanSetting getIgnoreCase() {
        return ignoreCase;
    }

    public static List<Integer> getMatchingSlots(List<ItemSlot> container, String query) {
        if (container == null || query == null || query.trim().isEmpty()) return Collections.emptyList();
        String q = query.trim().toLowerCase();
        List<Integer> matches = new ArrayList<>();
        for (int i = 0; i < container.size(); i++) {
            ItemSlot s = container.get(i);
            if (s != null && !s.isEmpty() && s.itemId() != null) {
                if (s.itemId().toLowerCase().contains(q) || (s.displayName() != null && s.displayName().toLowerCase().contains(q))) {
                    matches.add(i);
                }
            }
        }
        return matches;
    }
}
