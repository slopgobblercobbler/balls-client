package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * CropYieldPredictor — Calculates expected harvest drop yield based on crop type, growth stage, and Fortune bonus.
 */
public class CropYieldPredictor extends Module {
    private final NumberSetting fortuneTier = new NumberSetting("FortuneTier", "Fortune tool enchantment level", 3.0, 0.0, 3.0, 1.0);
    private final ModeSetting cropType = new ModeSetting("CropType", "Target agricultural crop", "Wheat", "Wheat", "Carrots", "Potatoes", "NetherWart");

    public CropYieldPredictor() {
        super("crop-yield-predictor", "Crop Yield Predictor", "Calculates expected harvest drop yield based on crop type, growth stage, and Fortune bonus", Category.AUTOMATION);
        addSettings(fortuneTier, cropType);
    }

    public NumberSetting getFortuneTier() {
        return fortuneTier;
    }

    public ModeSetting getCropType() {
        return cropType;
    }

    public double calculateExpectedYield() {
        double fortune = fortuneTier.getValue();
        String crop = cropType.getValue();
        double base = switch (crop) {
            case "Carrots", "Potatoes" -> 2.5;
            case "NetherWart" -> 2.5;
            default -> 1.0;
        };
        // Expected fortune bonus: fortune / 2
        return base + (fortune * 0.5);
    }
}
