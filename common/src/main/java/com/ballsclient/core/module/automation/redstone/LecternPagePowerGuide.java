package com.ballsclient.core.module.automation.redstone;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * LecternPagePowerGuide — Calculates book page count and current page needed to generate specific comparator signal.
 */
public class LecternPagePowerGuide extends Module {
    private final NumberSetting targetPower = new NumberSetting("TargetPower", "Desired comparator output signal level (1-15)", 15.0, 1.0, 15.0, 1.0);

    public LecternPagePowerGuide() {
        super("lectern-page-power-guide", "Lectern Page Power Guide", "Calculates book page count and current page needed to generate specific comparator signal", Category.AUTOMATION);
        addSettings(targetPower);
    }

    public NumberSetting getTargetPower() {
        return targetPower;
    }

    public static int calculateRequiredPage(int totalPages, int targetSignal) {
        if (totalPages <= 1 || targetSignal <= 1) {
            return 1;
        }
        int clampedSignal = Math.max(1, Math.min(15, targetSignal));
        double page = Math.ceil(((clampedSignal - 1) * (totalPages - 1)) / 14.0) + 1;
        return (int) Math.min(totalPages, page);
    }

    public int getPageForTarget(int totalPages) {
        return calculateRequiredPage(totalPages, (int) Math.round(targetPower.getValue()));
    }
}
