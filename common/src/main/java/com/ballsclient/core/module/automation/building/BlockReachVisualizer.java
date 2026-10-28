package com.ballsclient.core.module.automation.building;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * BlockReachVisualizer — Projects 3D spherical boundary showing exact 4.5-meter block interaction limit.
 */
public class BlockReachVisualizer extends Module {
    private final NumberSetting reachDistance = new NumberSetting("ReachDistance", "Maximum interaction sphere radius in blocks", 4.5, 3.0, 6.0, 0.1);
    private final BooleanSetting showEyeLine = new BooleanSetting("ShowEyeLine", "Renders vector line from player eyes to target block face", true);

    public BlockReachVisualizer() {
        super("block-reach-visualizer", "Block Reach Visualizer", "Projects 3D spherical boundary showing exact 4.5-meter block interaction limit", Category.AUTOMATION);
        addSettings(reachDistance, showEyeLine);
    }

    public NumberSetting getReachDistance() {
        return reachDistance;
    }

    public BooleanSetting getShowEyeLine() {
        return showEyeLine;
    }

    public boolean isWithinReach(double eyeX, double eyeY, double eyeZ, double targetX, double targetY, double targetZ) {
        double dx = targetX - eyeX;
        double dy = targetY - eyeY;
        double dz = targetZ - eyeZ;
        double r = reachDistance.getValue();
        return (dx * dx + dy * dy + dz * dz) <= (r * r);
    }
}
