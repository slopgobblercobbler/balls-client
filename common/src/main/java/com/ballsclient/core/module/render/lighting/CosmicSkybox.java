package com.ballsclient.core.module.render.lighting;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Procedural celestial sphere rendering rotating nebula clouds, galaxy spirals, and twin moons.
 */
public class CosmicSkybox extends Module {
    private final ModeSetting skyStyle = new ModeSetting("Style", "Cosmic sky preset", "NEBULA", "NEBULA", "GALAXY", "AURORA");
    private final NumberSetting rotationSpeed = new NumberSetting("RotationSpeed", "Skybox rotation rate multiplier", 1.0, 0.0, 5.0, 0.5);
    private final ColorSetting nebulaTint = new ColorSetting("NebulaTint", "Primary interstellar cloud tint", 0xFF660099);

    private float currentSkyAngle = 0.0f;

    public CosmicSkybox() {
        super("cosmic-skybox", "Cosmic Skybox", "Procedural cosmic sky dome with rotating galaxies and nebula clouds", Category.RENDER);
        addSettings(skyStyle, rotationSpeed, nebulaTint);
    }

    public void tickRotation(float deltaSeconds) {
        this.currentSkyAngle = (float) ((this.currentSkyAngle + deltaSeconds * rotationSpeed.getValue() * 2.0) % 360.0);
    }

    public float getCurrentSkyAngle() { return currentSkyAngle; }
    public String getSkyStyle() { return skyStyle.getValue(); }
    public int getNebulaTint() { return nebulaTint.getValue(); }
}
