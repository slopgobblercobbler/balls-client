package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * SubChunkCuller — Frustum and subterranean 16x16x16 section occlusion volume math.
 */
public class SubChunkCuller extends Module {
    private final NumberSetting fovAngle = new NumberSetting("FovAngle", "Frustum horizontal culling FOV", 90.0, 60.0, 120.0, 5.0);
    private final BooleanSetting cullSubterranean = new BooleanSetting("CullSubterranean", "Aggressively cull buried subterranean sections", true);
    private final NumberSetting subterraneanYThreshold = new NumberSetting("SubterraneanYThreshold", "Y elevation cutoff for buried sections", 0.0, -64.0, 64.0, 4.0);

    public SubChunkCuller() {
        super("sub-chunk-culler", "Sub Chunk Culler", "Frustum and subterranean 16x16x16 section occlusion volume math", Category.PLAYER);
        addSettings(fovAngle, cullSubterranean, subterraneanYThreshold);
    }

    public NumberSetting getFovAngle() {
        return fovAngle;
    }

    public BooleanSetting getCullSubterranean() {
        return cullSubterranean;
    }

    public NumberSetting getSubterraneanYThreshold() {
        return subterraneanYThreshold;
    }

    public static boolean isSectionInFrustum(double camX, double camY, double camZ, double yaw, double pitch, double fov,
                                             double sectionMinX, double sectionMinY, double sectionMinZ) {
        double centerX = sectionMinX + 8.0;
        double centerY = sectionMinY + 8.0;
        double centerZ = sectionMinZ + 8.0;

        double dx = centerX - camX;
        double dy = centerY - camY;
        double dz = centerZ - camZ;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist < 16.0) return true; // Always visible if adjacent

        double lookX = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double lookY = -Math.sin(Math.toRadians(pitch));
        double lookZ = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));

        double dot = (dx * lookX + dy * lookY + dz * lookZ) / dist;
        double angleToCenter = Math.toDegrees(Math.acos(Math.max(-1.0, Math.min(1.0, dot))));
        return angleToCenter <= (fov / 2.0) + 15.0; // Margin for section radius
    }
}
