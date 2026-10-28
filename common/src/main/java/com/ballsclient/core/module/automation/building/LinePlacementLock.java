package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;

/**
 * LinePlacementLock — Constrains block placement strictly to 1D cardinal axis lines (X or Z) for straight beams.
 */
public class LinePlacementLock extends Module {
    public record BlockPos(int x, int y, int z) {}

    private final ModeSetting axisLock = new ModeSetting("AxisLock", "Cardinal line constraint axis", "AutoCardinal", "AutoCardinal", "Axis_X", "Axis_Z");

    private Integer anchorX = null;
    private Integer anchorY = null;
    private Integer anchorZ = null;

    public LinePlacementLock() {
        super("line-placement-lock", "Line Placement Lock", "Constrains block placement strictly to 1D cardinal axis lines (X or Z) for straight beams", Category.AUTOMATION);
        addSettings(axisLock);
    }

    public ModeSetting getAxisLock() {
        return axisLock;
    }

    public void setAnchor(int x, int y, int z) {
        this.anchorX = x;
        this.anchorY = y;
        this.anchorZ = z;
    }

    public void clearAnchor() {
        this.anchorX = null;
        this.anchorY = null;
        this.anchorZ = null;
    }

    public BlockPos constrainPosition(int x, int y, int z) {
        if (anchorX == null || anchorZ == null) {
            setAnchor(x, y, z);
            return new BlockPos(x, y, z);
        }

        String mode = axisLock.getValue();
        int fixedY = anchorY != null ? anchorY : y;

        if ("Axis_X".equalsIgnoreCase(mode)) {
            return new BlockPos(x, fixedY, anchorZ);
        } else if ("Axis_Z".equalsIgnoreCase(mode)) {
            return new BlockPos(anchorX, fixedY, z);
        } else {
            // AutoCardinal: choose axis with larger displacement
            int dx = Math.abs(x - anchorX);
            int dz = Math.abs(z - anchorZ);
            if (dx >= dz) {
                return new BlockPos(x, fixedY, anchorZ);
            } else {
                return new BlockPos(anchorX, fixedY, z);
            }
        }
    }
}
