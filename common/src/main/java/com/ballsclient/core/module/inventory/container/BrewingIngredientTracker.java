package com.ballsclient.core.module.inventory.container;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * BrewingIngredientTracker — Monitors blaze powder fuel (20 brews/unit) and validates ingredient brew recipes.
 */
public class BrewingIngredientTracker extends Module {
    private final BooleanSetting trackBlazeFuel = new BooleanSetting("TrackBlazeFuel", "Monitor remaining blaze powder fuel units", true);

    public BrewingIngredientTracker() {
        super("brewing-ingredient-tracker", "Brewing Tracker", "Monitors blaze powder fuel and validates ingredient brew recipes", Category.INVENTORY);
        addSettings(trackBlazeFuel);
    }

    public BooleanSetting getTrackBlazeFuel() {
        return trackBlazeFuel;
    }

    public static int calculateRemainingBrews(int blazeFuelUnits) {
        return Math.max(0, blazeFuelUnits * 20);
    }

    public static boolean isValidIngredient(String ingredientId) {
        if (ingredientId == null) return false;
        String id = ingredientId.toLowerCase();
        return id.contains("nether_wart") || id.contains("redstone") || id.contains("glowstone_dust")
                || id.contains("fermented_spider_eye") || id.contains("gunpowder") || id.contains("dragon_breath")
                || id.contains("sugar") || id.contains("rabbit_foot") || id.contains("glistering_melon_slice")
                || id.contains("spider_eye") || id.contains("ghast_tear") || id.contains("magma_cream")
                || id.contains("pufferfish") || id.contains("golden_carrot") || id.contains("turtle_helmet")
                || id.contains("phantom_membrane");
    }
}
