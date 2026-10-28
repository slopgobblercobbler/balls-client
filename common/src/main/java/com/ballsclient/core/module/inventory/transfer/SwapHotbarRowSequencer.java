package com.ballsclient.core.module.inventory.transfer;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.module.inventory.SlotSwap;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * SwapHotbarRowSequencer — Swaps entire active hotbar row (Slots 0-8) with selected inventory row (9-17, 18-26, 27-35).
 */
public class SwapHotbarRowSequencer extends Module {
    private final NumberSetting targetInventoryRow = new NumberSetting("TargetInventoryRow", "Target inventory row to swap with hotbar (1-3)", 1.0, 1.0, 3.0, 1.0);

    public SwapHotbarRowSequencer() {
        super("swap-hotbar-row-sequencer", "Swap Hotbar Row", "Swaps entire active hotbar row with selected inventory row", Category.INVENTORY);
        addSettings(targetInventoryRow);
    }

    public NumberSetting getTargetInventoryRow() {
        return targetInventoryRow;
    }

    public static List<SlotSwap> planRowSwap(int targetRow) {
        int r = Math.max(1, Math.min(3, targetRow));
        int rowStart = 9 * r;
        List<SlotSwap> swaps = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            swaps.add(new SlotSwap(i, rowStart + i));
        }
        return swaps;
    }
}
