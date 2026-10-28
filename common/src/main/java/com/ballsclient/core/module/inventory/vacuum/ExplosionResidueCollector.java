package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * ExplosionResidueCollector — Sweeps TNT and creeper explosion debris immediately before items scatter or despawn.
 */
public class ExplosionResidueCollector extends Module {
    private final NumberSetting sweepRadius = new NumberSetting("SweepRadius", "Blast perimeter sweep radius", 8.0, 3.0, 12.0, 1.0);

    public ExplosionResidueCollector() {
        super("explosion-residue-collector", "Explosion Residue Collector", "Sweeps explosion debris before items scatter", Category.INVENTORY);
        addSettings(sweepRadius);
    }

    public NumberSetting getSweepRadius() {
        return sweepRadius;
    }

    public static boolean isWithinBlastPerimeter(double ex, double ey, double ez, double ix, double iy, double iz, double blastRadius) {
        double dx = ex - ix;
        double dy = ey - iy;
        double dz = ez - iz;
        return (dx * dx + dy * dy + dz * dz) <= (blastRadius * blastRadius);
    }
}
