package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Raycast Hitbox Viewer — Computes server-side expanded hitboxes accounting for eye elevation, server latency, and entity motion
 */
public class RaycastHitboxViewer extends Module {
    private final NumberSetting expansionMargin = new NumberSetting("ExpansionMargin", "Hitbox expansion margin in blocks", 0.1, 0.0, 0.5, 0.01);
    private final BooleanSetting accountForPing = new BooleanSetting("AccountForPing", "Expand bounds by target velocity times ping", true);

    public RaycastHitboxViewer() {
        super("raycast-hitbox-viewer", "Raycast Hitbox Viewer", "Computes server-side expanded hitboxes accounting for eye elevation, server latency, and entity motion", Category.COMBAT);
        addSettings(expansionMargin, accountForPing);
    }

    public double getExpansionMargin() {
        return expansionMargin.getValue();
    }

    public boolean isAccountForPing() {
        return accountForPing.getValue();
    }

    public double[] computeExpandedAABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, double margin, double vx, double vy, double vz, double latencySeconds) {
        double shiftX = vx * latencySeconds;
        double shiftY = vy * latencySeconds;
        double shiftZ = vz * latencySeconds;
        return new double[]{
            Math.min(minX, minX + shiftX) - margin,
            Math.min(minY, minY + shiftY) - margin,
            Math.min(minZ, minZ + shiftZ) - margin,
            Math.max(maxX, maxX + shiftX) + margin,
            Math.max(maxY, maxY + shiftY) + margin,
            Math.max(maxZ, maxZ + shiftZ) + margin
        };
    }
}
