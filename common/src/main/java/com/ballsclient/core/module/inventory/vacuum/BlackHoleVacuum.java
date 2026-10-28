package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BlackHoleVacuum — Simulates gravitational attraction field pulling ground items into player pickup radius.
 */
public class BlackHoleVacuum extends Module {
    private final NumberSetting vacuumRadius = new NumberSetting("VacuumRadius", "Max attraction radius in meters", 6.0, 1.0, 12.0, 0.5);
    private final NumberSetting gravitationalConstant = new NumberSetting("GravitationalConstant", "Attraction strength constant G", 1.2, 0.1, 5.0, 0.1);

    public BlackHoleVacuum() {
        super("black-hole-vacuum", "Black Hole Vacuum", "Simulates gravitational attraction field pulling ground items", Category.INVENTORY);
        addSettings(vacuumRadius, gravitationalConstant);
    }

    public NumberSetting getVacuumRadius() {
        return vacuumRadius;
    }

    public NumberSetting getGravitationalConstant() {
        return gravitationalConstant;
    }

    public static double[] computeGravitationalPull(double px, double py, double pz, double ix, double iy, double iz, double G, double radius) {
        double dx = px - ix;
        double dy = py - iy;
        double dz = pz - iz;
        double distSq = dx * dx + dy * dy + dz * dz;
        if (distSq <= 0.0001 || distSq > (radius * radius)) {
            return new double[]{0.0, 0.0, 0.0};
        }
        double dist = Math.sqrt(distSq);
        double force = G / Math.max(1.0, distSq);
        return new double[]{
                (dx / dist) * force,
                (dy / dist) * force,
                (dz / dist) * force
        };
    }
}
