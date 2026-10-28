package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * RoofPitchAssistant — Assists in building gable and hip roofs by calculating stair and slab overhang steps.
 */
public class RoofPitchAssistant extends Module {
    private final ModeSetting roofStyle = new ModeSetting("RoofStyle", "Geometric slope pattern", "Gable_1_1", "Gable_1_1", "Mansard", "Steep_2_1");
    private final BooleanSetting overhang = new BooleanSetting("Overhang", "Extends eaves 1 block past exterior wall", true);

    public RoofPitchAssistant() {
        super("roof-pitch-assistant", "Roof Pitch Assistant", "Assists in building gable and hip roofs by calculating stair and slab overhang steps", Category.AUTOMATION);
        addSettings(roofStyle, overhang);
    }

    public ModeSetting getRoofStyle() {
        return roofStyle;
    }

    public BooleanSetting getOverhang() {
        return overhang;
    }

    public int computeHeightAtOffset(int horizontalOffset) {
        String style = roofStyle.getValue();
        if ("Steep_2_1".equalsIgnoreCase(style)) {
            return horizontalOffset * 2;
        } else if ("Mansard".equalsIgnoreCase(style)) {
            return horizontalOffset <= 2 ? horizontalOffset * 2 : 4 + (horizontalOffset - 2);
        }
        return horizontalOffset;
    }
}
