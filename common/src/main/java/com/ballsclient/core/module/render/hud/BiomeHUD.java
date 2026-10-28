package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Dedicated HUD element displaying current biome identity and thermal rating.
 */
public class BiomeHUD extends Module {
    private final BooleanSetting showTemperature = new BooleanSetting("ShowTemperature", "Show numerical temperature value", true);
    private final ColorSetting textColor = new ColorSetting("TextColor", "Biome name text color", 0xFF88FF88);

    private String currentBiome = "Plains";
    private float temperature = 0.8f;

    public BiomeHUD() {
        super("biome-hud", "Biome HUD", "Displays current biome and thermal profile", Category.RENDER);
        addSettings(showTemperature, textColor);
    }

    public void updateBiome(String biome, float temperature) {
        this.currentBiome = biome != null ? biome : "Plains";
        this.temperature = temperature;
    }

    public String formatDisplayText() {
        if (showTemperature.getValue()) {
            return String.format("Biome: %s (%.1f)", currentBiome, temperature);
        }
        return "Biome: " + currentBiome;
    }

    public String getCurrentBiome() { return currentBiome; }
    public float getTemperature() { return temperature; }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render biome HUD
    }
}
