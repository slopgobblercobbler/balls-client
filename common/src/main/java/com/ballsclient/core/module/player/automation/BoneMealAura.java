package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BoneMealAura — Applies bone meal to immature crops and saplings within interaction radius.
 */
public class BoneMealAura extends Module {
    private final NumberSetting applyRadius = new NumberSetting("ApplyRadius", "Radial bone meal dispersion reach in blocks", 4.0, 1.0, 6.0, 0.5);
    private final BooleanSetting saplings = new BooleanSetting("Saplings", "Fertilize saplings and fungus", true);
    private final BooleanSetting crops = new BooleanSetting("Crops", "Fertilize immature wheat, carrot, potato crops", true);

    public BoneMealAura() {
        super("bone-meal-aura", "Bone Meal Aura", "Applies bone meal to immature crops and saplings within interaction radius", Category.PLAYER);
        addSettings(applyRadius, saplings, crops);
    }

    public NumberSetting getApplyRadius() {
        return applyRadius;
    }

    public BooleanSetting getSaplings() {
        return saplings;
    }

    public BooleanSetting getCrops() {
        return crops;
    }

    public static boolean canApplyBoneMeal(int currentAge, int maxAge, boolean isSapling) {
        return currentAge < maxAge || isSapling;
    }
}
