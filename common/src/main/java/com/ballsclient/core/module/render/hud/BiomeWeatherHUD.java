package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * Environmental indicator displaying current biome name, temperature, and light levels.
 */
public class BiomeWeatherHUD extends Module {
    private final BooleanSetting showLightLevel = new BooleanSetting("ShowLightLevel", "Display block and sky light levels", true);
    private final ColorSetting textColor = new ColorSetting("TextColor", "Text display color", 0xFF55FFFF);

    private String biome = "Plains";
    private float temperature = 0.8f;
    private int blockLight = 15;
    private int skyLight = 15;
    private boolean raining = false;

    public BiomeWeatherHUD() {
        super("biome-weather-hud", "Biome & Weather HUD", "Displays environmental biome, temperature, and light telemetry", Category.RENDER);
        addSettings(showLightLevel, textColor);
    }

    public void updateEnvironment(String biome, float temperature, int blockLight, int skyLight, boolean raining) {
        this.biome = biome != null ? biome : "Plains";
        this.temperature = temperature;
        this.blockLight = Math.max(0, Math.min(15, blockLight));
        this.skyLight = Math.max(0, Math.min(15, skyLight));
        this.raining = raining;
    }

    public String formatDisplayText() {
        String weatherStr = raining ? "Rain" : "Clear";
        if (showLightLevel.getValue()) {
            return String.format("%s | Temp: %.1f | %s | Light: %d (Sky: %d)", biome, temperature, weatherStr, blockLight, skyLight);
        }
        return String.format("%s | Temp: %.1f | %s", biome, temperature, weatherStr);
    }

    public String getBiome() { return biome; }
    public float getTemperature() { return temperature; }
    public int getBlockLight() { return blockLight; }
    public int getSkyLight() { return skyLight; }
    public boolean isRaining() { return raining; }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render environment HUD
    }
}
