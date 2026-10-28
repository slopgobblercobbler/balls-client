package com.ballsclient.core.module.inventory.vacuum;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * CactusProximityShield — Deflects nearby dropped items away from cactus block faces to prevent destruction.
 */
public class CactusProximityShield extends Module {
    private final NumberSetting deflectionImpulse = new NumberSetting("DeflectionImpulse", "Deflection impulse acceleration magnitude", 0.4, 0.1, 1.0, 0.05);

    public CactusProximityShield() {
        super("cactus-proximity-shield", "Cactus Proximity Shield", "Deflects nearby dropped items away from cactus blocks", Category.INVENTORY);
        addSettings(deflectionImpulse);
    }

    public NumberSetting getDeflectionImpulse() {
        return deflectionImpulse;
    }

    public static double[] computeDeflectionVector(double ix, double iz, double cx, double cz, double impulse) {
        double dx = ix - cx;
        double dz = iz - cz;
        double distSq = dx * dx + dz * dz;
        if (distSq <= 0.0001) return new double[]{impulse, impulse};
        double dist = Math.sqrt(distSq);
        return new double[]{
                (dx / dist) * impulse,
                (dz / dist) * impulse
        };
    }
}
