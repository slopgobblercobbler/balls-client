package com.ballsclient.core.module.combat.defense;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Anchor Defense — Monitors respawn anchor glow levels and glowstone charges to compute lethal zones
 */
public class AnchorDefense extends Module {
    private final NumberSetting anchorScanRange = new NumberSetting("AnchorScanRange", "Perimeter range to scan for respawn anchors", 5.5, 3.0, 8.0, 0.5);
    private final BooleanSetting warnAtMaxCharge = new BooleanSetting("WarnAtMaxCharge", "Issue loud alert when anchor reaches charge 4", true);

    public AnchorDefense() {
        super("anchor-defense", "Anchor Defense", "Monitors respawn anchor glow levels and glowstone charges to compute lethal zones", Category.COMBAT);
        addSettings(anchorScanRange, warnAtMaxCharge);
    }

    public double getAnchorScanRange() {
        return anchorScanRange.getValue();
    }

    public boolean isWarnAtMaxCharge() {
        return warnAtMaxCharge.getValue();
    }

    public boolean isAnchorFullyCharged(int charges) {
        return charges >= 4;
    }
}
