package com.ballsclient.core.module.movement.failsafe;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Automatically sneaks or hops over magma blocks to prevent heat damage.
 */
public class MagmaBlockFailsafe extends Module {
    private final BooleanSetting autoSneak = new BooleanSetting("AutoSneak", "Automatically pulse sneak while walking over magma blocks", true);
    private final BooleanSetting frostWalkerSpoof = new BooleanSetting("FrostWalkerSpoof", "Spoof Frost Walker enchantment status to ignore damage", false);

    public MagmaBlockFailsafe() {
        super("magma-block-failsafe", "Magma Block Failsafe", "Automatically sneaks or hops over magma blocks to prevent heat damage", Category.MOVEMENT);
        addSettings(autoSneak, frostWalkerSpoof);
    }

    public boolean isAutoSneak() { return autoSneak.getValue(); }
    public boolean isFrostWalkerSpoof() { return frostWalkerSpoof.getValue(); }

    public boolean shouldAutoSneak(boolean standingOnMagma) {
        return isEnabled() && isAutoSneak() && standingOnMagma;
    }
}
