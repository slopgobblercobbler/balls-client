package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Direct controller for forcing specific lunar phases regardless of world day counter.
 */
public class MoonPhaseChanger extends Module {
    private final ModeSetting phase = new ModeSetting("Phase", "Forced moon phase", "FULL",
            "FULL", "WANING_GIBBOUS", "THIRD_QUARTER", "WANING_CRESCENT", "NEW", "WAXING_CRESCENT", "FIRST_QUARTER", "WAXING_GIBBOUS");

    public MoonPhaseChanger() {
        super("moon-phase-changer", "Moon Phase Changer", "Forces visual client moon phase to any of the 8 lunar stages", Category.RENDER);
        addSettings(phase);
    }

    public int getPhaseIndex() {
        return switch (phase.getValue().toUpperCase()) {
            case "FULL" -> 0;
            case "WANING_GIBBOUS" -> 1;
            case "THIRD_QUARTER" -> 2;
            case "WANING_CRESCENT" -> 3;
            case "NEW" -> 4;
            case "WAXING_CRESCENT" -> 5;
            case "FIRST_QUARTER" -> 6;
            case "WAXING_GIBBOUS" -> 7;
            default -> 0;
        };
    }

    public String getPhaseName() { return phase.getValue(); }
}
