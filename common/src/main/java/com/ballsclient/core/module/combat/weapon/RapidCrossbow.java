package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Rapid Crossbow — Cycles through pre-loaded hotbar crossbows for instant volley firing
 */
public class RapidCrossbow extends Module {
    private final NumberSetting volleyDelayMs = new NumberSetting("VolleyDelayMs", "Delay between consecutive crossbow discharges", 100.0, 50.0, 250.0, 10.0);
    private final BooleanSetting autoReloadWhenSafe = new BooleanSetting("AutoReloadWhenSafe", "Reload empty crossbows when no enemies nearby", true);

    public RapidCrossbow() {
        super("rapid-crossbow", "Rapid Crossbow", "Cycles through pre-loaded hotbar crossbows for instant volley firing", Category.COMBAT);
        addSettings(volleyDelayMs, autoReloadWhenSafe);
    }

    public double getVolleyDelayMs() {
        return volleyDelayMs.getValue();
    }

    public boolean isAutoReloadWhenSafe() {
        return autoReloadWhenSafe.getValue();
    }

    public int getNextLoadedCrossbowSlot(boolean[] isLoadedCrossbow, int currentSlot) {
        for (int i = 0; i < isLoadedCrossbow.length; i++) {
            int slot = (currentSlot + 1 + i) % isLoadedCrossbow.length;
            if (isLoadedCrossbow[slot]) return slot;
        }
        return -1;
    }
}
