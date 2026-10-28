package com.ballsclient.core.module.render.cosmetic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Miniature cosmetic companion (Mini-Ghast, Floating Orb, Fairy) orbiting player shoulder.
 */
public class PetCosmetic extends Module {
    private final ModeSetting petType = new ModeSetting("Type", "Companion creature model", "ORB", "ORB", "MINI_GHAST", "FAIRY");
    private final NumberSetting orbitSpeed = new NumberSetting("OrbitSpeed", "Shoulder orbit revolution speed", 1.2, 0.1, 4.0, 0.1);

    public PetCosmetic() {
        super("pet-cosmetic", "Pet Cosmetic", "Miniature companion entity orbiting player shoulder", Category.RENDER);
        addSettings(petType, orbitSpeed);
    }

    public double[] calculateOrbitPosition(double playerX, double playerY, double playerZ, long timeMillis) {
        double angle = (timeMillis * 0.002 * orbitSpeed.getValue());
        double radius = 1.0;
        double ox = playerX + Math.cos(angle) * radius;
        double oy = playerY + 1.2 + Math.sin(angle * 2.0) * 0.15;
        double oz = playerZ + Math.sin(angle) * radius;
        return new double[]{ox, oy, oz};
    }

    public String getPetType() { return petType.getValue(); }
    public double getOrbitSpeed() { return orbitSpeed.getValue(); }
}
