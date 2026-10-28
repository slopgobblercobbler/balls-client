package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BiomeLocator — Multi-noise climate coordinate vector evaluator toward target biomes.
 */
public class BiomeLocator extends Module {
    private final ModeSetting targetBiome = new ModeSetting("TargetBiome", "Target climate biome", "CherryGrove", "MushroomFields", "CherryGrove", "DeepDark", "Badlands", "IceSpikes");
    private final NumberSetting searchRadius = new NumberSetting("SearchRadius", "Radial search boundary", 2000.0, 500.0, 10000.0, 250.0);
    private final BooleanSetting audioPing = new BooleanSetting("AudioPing", "Sound ping when entering biome perimeter", true);

    public BiomeLocator() {
        super("biome-locator", "Biome Locator", "Multi-noise climate coordinate vector evaluator toward target biomes", Category.PLAYER);
        addSettings(targetBiome, searchRadius, audioPing);
    }

    public ModeSetting getTargetBiome() {
        return targetBiome;
    }

    public NumberSetting getSearchRadius() {
        return searchRadius;
    }

    public BooleanSetting getAudioPing() {
        return audioPing;
    }

    public static double calculateMultiNoiseFitness(double temp, double hum, double cont, double ero, double targetT, double targetH, double targetC, double targetE) {
        double dt = temp - targetT;
        double dh = hum - targetH;
        double dc = cont - targetC;
        double de = ero - targetE;
        return Math.sqrt(dt * dt + dh * dh + dc * dc + de * de);
    }
}
