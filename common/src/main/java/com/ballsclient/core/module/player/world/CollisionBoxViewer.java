package com.ballsclient.core.module.player.world;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

/**
 * CollisionBoxViewer — Complex voxel shape raycast and AABB intersection evaluator.
 */
public class CollisionBoxViewer extends Module {
    private final BooleanSetting showOutline = new BooleanSetting("ShowOutline", "Render wireframe voxel bounding box", true);
    private final BooleanSetting showSolidOnly = new BooleanSetting("ShowSolidOnly", "Only evaluate solid collision voxels", false);
    private final ColorSetting outlineColor = new ColorSetting("OutlineColor", "Bounding box line color", 0xFF00FFFF);

    public CollisionBoxViewer() {
        super("collision-box-viewer", "Collision Box Viewer", "Complex voxel shape raycast and AABB intersection evaluator", Category.PLAYER);
        addSettings(showOutline, showSolidOnly, outlineColor);
    }

    public BooleanSetting getShowOutline() {
        return showOutline;
    }

    public BooleanSetting getShowSolidOnly() {
        return showSolidOnly;
    }

    public ColorSetting getOutlineColor() {
        return outlineColor;
    }

    public static boolean rayIntersectsAABB(double rx, double ry, double rz, double rdx, double rdy, double rdz,
                                            double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double t1 = (minX - rx) / (rdx != 0 ? rdx : 1e-9);
        double t2 = (maxX - rx) / (rdx != 0 ? rdx : 1e-9);
        double t3 = (minY - ry) / (rdy != 0 ? rdy : 1e-9);
        double t4 = (maxY - ry) / (rdy != 0 ? rdy : 1e-9);
        double t5 = (minZ - rz) / (rdz != 0 ? rdz : 1e-9);
        double t6 = (maxZ - rz) / (rdz != 0 ? rdz : 1e-9);

        double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        if (tmax < 0) return false;
        return tmin <= tmax;
    }
}
