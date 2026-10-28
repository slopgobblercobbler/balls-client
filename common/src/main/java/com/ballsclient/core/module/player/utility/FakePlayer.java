package com.ballsclient.core.module.player.utility;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * FakePlayer — Spawns a client-side mock player entity for damage and hitbox calibration.
 */
public class FakePlayer extends Module {
    private final ModeSetting skinModel = new ModeSetting("SkinModel", "Player model geometry type", "Default", "Default", "Slim");
    private final NumberSetting spawnDistance = new NumberSetting("SpawnDistance", "Spawn distance in front of player eyes", 2.0, 1.0, 10.0, 0.5);
    private final BooleanSetting copyEquipment = new BooleanSetting("CopyEquipment", "Mirror current player armor and held items", true);

    public FakePlayer() {
        super("fake-player", "Fake Player", "Spawns a client-side mock player entity for damage and hitbox calibration", Category.PLAYER);
        addSettings(skinModel, spawnDistance, copyEquipment);
    }

    public ModeSetting getSkinModel() {
        return skinModel;
    }

    public NumberSetting getSpawnDistance() {
        return spawnDistance;
    }

    public BooleanSetting getCopyEquipment() {
        return copyEquipment;
    }

    public static double[] getSpawnCoords(double px, double py, double pz, float yaw, double distance) {
        double rad = Math.toRadians(yaw);
        double spawnX = px - Math.sin(rad) * distance;
        double spawnZ = pz + Math.cos(rad) * distance;
        return new double[]{spawnX, py, spawnZ};
    }
}
