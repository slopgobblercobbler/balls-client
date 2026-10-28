package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Edge-snapping and alignment assist for continuous block bridging.
 */
public class ScaffoldHelper extends Module {
    private final BooleanSetting edgeSnap = new BooleanSetting("EdgeSnap", "Snap player coordinates to block center axis while bridging", true);
    private final BooleanSetting descendingSneak = new BooleanSetting("DescendingSneak", "Automatically sneak when descending scaffold blocks", true);
    private final NumberSetting snapThreshold = new NumberSetting("SnapThreshold", "Distance threshold to snap to block axis", 0.15, 0.05, 0.4, 0.01);

    public ScaffoldHelper() {
        super("scaffold-helper", "Scaffold Helper", "Edge-snapping and alignment assist for continuous block bridging", Category.MOVEMENT);
        addSettings(edgeSnap, descendingSneak, snapThreshold);
    }

    public boolean isEdgeSnap() { return edgeSnap.getValue(); }
    public boolean isDescendingSneak() { return descendingSneak.getValue(); }
    public double getSnapThreshold() { return snapThreshold.getValue(); }

    public double calculateSnapPosition(double coord) {
        if (!isEnabled() || !isEdgeSnap()) return coord;
        double center = Math.floor(coord) + 0.5;
        if (Math.abs(coord - center) < getSnapThreshold()) {
            return center;
        }
        return coord;
    }

    public boolean shouldSneakAtEdge(double distToEdge) {
        return isEnabled() && isDescendingSneak() && distToEdge <= getSnapThreshold();
    }
}
