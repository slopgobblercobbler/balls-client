package com.ballsclient.core.module.automation.schematic;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * SchematicWaypointAnchor — Pins multi-structure placement anchors to world coordinates with persistent markers.
 */
public class SchematicWaypointAnchor extends Module {
    private final BooleanSetting anchorName = new BooleanSetting("AnchorName", "Renders world tag name above anchor", true);
    private final BooleanSetting beaconBeam = new BooleanSetting("BeaconBeam", "Renders vertical light beam at origin", true);

    private int anchorX = 0;
    private int anchorY = 64;
    private int anchorZ = 0;

    public SchematicWaypointAnchor() {
        super("schematic-waypoint-anchor", "Schematic Waypoint Anchor", "Pins multi-structure placement anchors to world coordinates with persistent markers", Category.AUTOMATION);
        addSettings(anchorName, beaconBeam);
    }

    public BooleanSetting getAnchorName() {
        return anchorName;
    }

    public BooleanSetting getBeaconBeam() {
        return beaconBeam;
    }

    public void setAnchor(int x, int y, int z) {
        this.anchorX = x;
        this.anchorY = y;
        this.anchorZ = z;
    }

    public int getAnchorX() {
        return anchorX;
    }

    public int getAnchorY() {
        return anchorY;
    }

    public int getAnchorZ() {
        return anchorZ;
    }

    public double distanceTo(double x, double y, double z) {
        double dx = anchorX - x;
        double dy = anchorY - y;
        double dz = anchorZ - z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
