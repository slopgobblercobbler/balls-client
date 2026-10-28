package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Armor Swapper Chords — Hotkeys for instantaneous hot-swapping between Elytra and Protection chestplate
 */
public class ArmorSwapperChords extends Module {
    private final ModeSetting defaultChestplate = new ModeSetting("DefaultChestplate", "Standard combat chestplate tier", "Netherite", "Netherite", "Diamond", "Elytra");
    private final BooleanSetting autoSwapOnFall = new BooleanSetting("AutoSwapOnFall", "Equip Elytra if fall height exceeds 4 blocks", true);

    public ArmorSwapperChords() {
        super("armor-swapper-chords", "Armor Swapper Chords", "Hotkeys for instantaneous hot-swapping between Elytra and Protection chestplate", Category.COMBAT);
        addSettings(defaultChestplate, autoSwapOnFall);
    }

    public String getDefaultChestplate() {
        return defaultChestplate.getValue();
    }

    public boolean isAutoSwapOnFall() {
        return autoSwapOnFall.getValue();
    }

    public boolean shouldSwapToElytra(boolean isFalling, double fallDistance, boolean elytraEquipped) {
        return isFalling && fallDistance > 4.0 && !elytraEquipped;
    }
}
