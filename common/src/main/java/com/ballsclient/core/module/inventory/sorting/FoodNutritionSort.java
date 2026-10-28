package com.ballsclient.core.module.inventory.sorting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * FoodNutritionSort — Ranks food items by effective nourishment score (restored food points + saturation).
 */
public class FoodNutritionSort extends Module {
    private final NumberSetting nutritionWeight = new NumberSetting("NutritionWeight", "Weight factor for restored hunger points", 1.0, 0.0, 2.0, 0.1);
    private final NumberSetting saturationWeight = new NumberSetting("SaturationWeight", "Weight factor for restored saturation points", 1.5, 0.0, 2.0, 0.1);

    public FoodNutritionSort() {
        super("food-nutrition-sort", "Food Nutrition Sort", "Ranks food items by effective nourishment score", Category.INVENTORY);
        addSettings(nutritionWeight, saturationWeight);
    }

    public NumberSetting getNutritionWeight() {
        return nutritionWeight;
    }

    public NumberSetting getSaturationWeight() {
        return saturationWeight;
    }

    public static double calculateFoodScore(int hunger, float saturationRatio, double nWeight, double sWeight) {
        return nWeight * hunger + sWeight * (hunger * saturationRatio * 2.0);
    }
}
