package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * RedstoneDustCrossTalkAuditor — Detects unintended signal cross-talk, quasiconnectivity, and budded power connections.
 */
public class RedstoneDustCrossTalkAuditor extends Module {
    private final BooleanSetting checkQuasiconnectivity = new BooleanSetting("CheckQuasiconnectivity", "Flags pistons, droppers, and dispensers susceptible to QC activation", true);

    public RedstoneDustCrossTalkAuditor() {
        super("redstone-dust-cross-talk-auditor", "Redstone Cross-Talk Auditor", "Detects unintended signal cross-talk, quasiconnectivity, and budded power connections", Category.AUTOMATION);
        addSettings(checkQuasiconnectivity);
    }

    public BooleanSetting getCheckQuasiconnectivity() {
        return checkQuasiconnectivity;
    }

    public static boolean isSusceptibleToQC(String blockId) {
        if (blockId == null) return false;
        String lower = blockId.toLowerCase();
        return lower.contains("piston") || lower.contains("dispenser") || lower.contains("dropper");
    }
}
