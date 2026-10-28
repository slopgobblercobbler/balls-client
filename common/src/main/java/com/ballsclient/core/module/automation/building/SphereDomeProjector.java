package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * SphereDomeProjector — Computes voxel coordinates for hollow or solid spheres, geodes, and architectural domes.
 */
public class SphereDomeProjector extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final NumberSetting radius = new NumberSetting("Radius", "Sphere radius in blocks", 10.0, 3.0, 48.0, 1.0);
    private final BooleanSetting domeOnly = new BooleanSetting("DomeOnly", "Truncates lower hemisphere for domes", false);
    private final BooleanSetting hollow = new BooleanSetting("Hollow", "Renders hollow perimeter shell only", true);

    public SphereDomeProjector() {
        super("sphere-dome-projector", "Sphere & Dome Projector", "Computes voxel coordinates for hollow or solid spheres, geodes, and architectural domes", Category.AUTOMATION);
        addSettings(radius, domeOnly, hollow);
    }

    public NumberSetting getRadius() {
        return radius;
    }

    public BooleanSetting getDomeOnly() {
        return domeOnly;
    }

    public BooleanSetting getHollow() {
        return hollow;
    }

    public static List<BlockPos> generateSpherePositions(int centerX, int centerY, int centerZ, double rad, boolean isDomeOnly, boolean isHollow) {
        List<BlockPos> positions = new ArrayList<>();
        int r = (int) Math.round(rad);
        double rInnerSq = (rad - 0.75) * (rad - 0.75);
        double rOuterSq = (rad + 0.25) * (rad + 0.25);
        int minY = isDomeOnly ? 0 : -r;

        for (int dy = minY; dy <= r; dy++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    double distSq = dx * dx + dy * dy + dz * dz;
                    if (isHollow) {
                        if (distSq >= rInnerSq && distSq <= rOuterSq) {
                            positions.add(new BlockPos(centerX + dx, centerY + dy, centerZ + dz));
                        }
                    } else {
                        if (distSq <= rOuterSq) {
                            positions.add(new BlockPos(centerX + dx, centerY + dy, centerZ + dz));
                        }
                    }
                }
            }
        }
        return positions;
    }
}
