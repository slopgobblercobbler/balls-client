package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * WorldBorderGuard — Barrier proximity detection, velocity damping, and suffocation buffer.
 */
public class WorldBorderGuard extends Module {
    private final NumberSetting warningBuffer = new NumberSetting("WarningBuffer", "Perimeter distance buffer to trigger warning", 10.0, 2.0, 50.0, 1.0);
    private final BooleanSetting cancelVelocityTowardsBorder = new BooleanSetting("CancelVelocityTowardsBorder", "Neutralize movement vectors toward border", true);
    private final BooleanSetting audioSiren = new BooleanSetting("AudioSiren", "Trigger warning siren near boundary", true);

    public WorldBorderGuard() {
        super("world-border-guard", "World Border Guard", "Barrier proximity detection, velocity damping, and suffocation buffer", Category.PLAYER);
        addSettings(warningBuffer, cancelVelocityTowardsBorder, audioSiren);
    }

    public NumberSetting getWarningBuffer() {
        return warningBuffer;
    }

    public BooleanSetting getCancelVelocityTowardsBorder() {
        return cancelVelocityTowardsBorder;
    }

    public BooleanSetting getAudioSiren() {
        return audioSiren;
    }

    public static double getDistanceToBorder(double px, double pz, double centerX, double centerZ, double borderDiameter) {
        double radius = borderDiameter / 2.0;
        double minX = centerX - radius;
        double maxX = centerX + radius;
        double minZ = centerZ - radius;
        double maxZ = centerZ + radius;

        double distWest = Math.abs(px - minX);
        double distEast = Math.abs(maxX - px);
        double distNorth = Math.abs(pz - minZ);
        double distSouth = Math.abs(maxZ - pz);

        return Math.min(Math.min(distWest, distEast), Math.min(distNorth, distSouth));
    }

    public static boolean isApproachingBorder(double px, double pz, double vx, double vz, double centerX, double centerZ, double borderDiameter) {
        double distCurrent = getDistanceToBorder(px, pz, centerX, centerZ, borderDiameter);
        double distNext = getDistanceToBorder(px + vx, pz + vz, centerX, centerZ, borderDiameter);
        return distNext < distCurrent;
    }
}
