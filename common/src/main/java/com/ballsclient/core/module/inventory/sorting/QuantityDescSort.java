package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.ItemSlot;
import com.ballsclient.core.setting.BooleanSetting;

import java.util.Comparator;

/**
 * QuantityDescSort — Orders inventory slots strictly by total item stack count from 64 down to 1.
 */
public class QuantityDescSort extends Module {
    private final BooleanSetting emptySlotsAtEnd = new BooleanSetting("EmptySlotsAtEnd", "Place empty slots at the end of the inventory", true);

    public QuantityDescSort() {
        super("quantity-desc-sort", "Quantity Desc Sort", "Orders inventory slots strictly by total item stack count from 64 down to 1", Category.INVENTORY);
        addSettings(emptySlotsAtEnd);
    }

    public BooleanSetting getEmptySlotsAtEnd() {
        return emptySlotsAtEnd;
    }

    public static Comparator<ItemSlot> createQuantityComparator(boolean descending, boolean emptyAtEnd) {
        return (a, b) -> {
            boolean aEmpty = (a == null || a.isEmpty());
            boolean bEmpty = (b == null || b.isEmpty());
            if (aEmpty && bEmpty) return 0;
            if (aEmpty) return emptyAtEnd ? 1 : -1;
            if (bEmpty) return emptyAtEnd ? -1 : 1;

            int countA = a.count();
            int countB = b.count();
            int cmp = Integer.compare(countA, countB);
            return descending ? -cmp : cmp;
        };
    }
}
