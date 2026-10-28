package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * CircleCylinderGuide — Projects 3D mathematical circle and cylinder voxel outlines with custom radius and height.
 */
public class CircleCylinderGuide extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final NumberSetting radius = new NumberSetting("Radius", "Cylinder radius in blocks", 8.0, 2.0, 64.0, 1.0);
    private final NumberSetting height = new NumberSetting("Height", "Cylinder extrusion height in blocks", 1.0, 1.0, 32.0, 1.0);
    private final BooleanSetting hollow = new BooleanSetting("Hollow", "Renders hollow perimeter wall only", true);

    public CircleCylinderGuide() {
        super("circle-cylinder-guide", "Circle & Cylinder Guide", "Projects 3D mathematical circle and cylinder voxel outlines with custom radius and height", Category.AUTOMATION);
        addSettings(radius, height, hollow);
    }

    public NumberSetting getRadius() {
        return radius;
    }

    public NumberSetting getHeightSetting() {
        return height;
    }

    public BooleanSetting getHollow() {
        return hollow;
    }

    public static List<BlockPos> generateCirclePositions(int centerX, int centerY, int centerZ, double rad, int h, boolean isHollow) {
        List<BlockPos> positions = new ArrayList<>();
        int r = (int) Math.round(rad);
        double rInnerSq = (rad - 0.75) * (rad - 0.75);
        double rOuterSq = (rad + 0.25) * (rad + 0.25);

        for (int dy = 0; dy < h; dy++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    double distSq = dx * dx + dz * dz;
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
