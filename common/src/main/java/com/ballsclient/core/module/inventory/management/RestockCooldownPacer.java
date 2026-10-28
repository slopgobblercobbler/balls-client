package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * RestockCooldownPacer — Paces inventory restocking requests to prevent anticheat fast-click penalties.
 */
public class RestockCooldownPacer extends Module {
    private final NumberSetting clickIntervalTicks = new NumberSetting("ClickIntervalTicks", "Ticks between sequential restock click operations", 2.0, 1.0, 10.0, 1.0);

    public RestockCooldownPacer() {
        super("restock-cooldown-pacer", "Restock Cooldown Pacer", "Paces restocking requests to prevent anticheat flags", Category.INVENTORY);
        addSettings(clickIntervalTicks);
    }

    public NumberSetting getClickIntervalTicks() {
        return clickIntervalTicks;
    }
}
