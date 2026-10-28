package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * BeaconEffectSelector — Automatically selects and confirms primary/secondary beacon powers on open.
 */
public class BeaconEffectSelector extends Module {
    private final ModeSetting primaryPower = new ModeSetting("PrimaryPower", "Primary beacon status effect", "Haste", "Haste", "Speed", "Resistance", "JumpBoost", "Strength");
    private final BooleanSetting autoConfirm = new BooleanSetting("AutoConfirm", "Instantly spend payment ingot to activate power", true);

    public BeaconEffectSelector() {
        super("beacon-effect-selector", "Beacon Effect Selector", "Automatically selects and confirms primary/secondary beacon powers on open", Category.PLAYER);
        addSettings(primaryPower, autoConfirm);
    }

    public ModeSetting getPrimaryPower() {
        return primaryPower;
    }

    public BooleanSetting getAutoConfirm() {
        return autoConfirm;
    }

    public static int getPowerEffectId(String powerName) {
        if ("Speed".equalsIgnoreCase(powerName)) return 1;
        if ("Haste".equalsIgnoreCase(powerName)) return 3;
        if ("Resistance".equalsIgnoreCase(powerName)) return 11;
        if ("JumpBoost".equalsIgnoreCase(powerName)) return 8;
        if ("Strength".equalsIgnoreCase(powerName)) return 5;
        return 3; // Haste default
    }
}
