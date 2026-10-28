package com.ballsclient.core.module.inventory.management;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * OffhandTotemAutoRefill — Automatically refills offhand slot with a Totem of Undying from inventory.
 */
public class OffhandTotemAutoRefill extends Module {
    private final BooleanSetting immediateRefill = new BooleanSetting("ImmediateRefill", "Refill offhand within 0 ticks of totem pop", true);

    public OffhandTotemAutoRefill() {
        super("offhand-totem-auto-refill", "Offhand Totem Auto Refill", "Automatically refills offhand slot with a Totem", Category.INVENTORY);
        addSettings(immediateRefill);
    }

    public BooleanSetting getImmediateRefill() {
        return immediateRefill;
    }
}
