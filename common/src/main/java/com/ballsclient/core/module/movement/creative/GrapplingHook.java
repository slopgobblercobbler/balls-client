package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Reels player toward fishing hook bobbers at high velocity.
 */
public class GrapplingHook extends Module {
    private final NumberSetting pullSpeed = new NumberSetting("PullSpeed", "Reel attraction speed along grappling cable", 1.6, 0.5, 4.0, 0.1);
    private final NumberSetting slackDistance = new NumberSetting("SlackDistance", "Distance threshold before cable tension pulls player", 2.0, 0.5, 5.0, 0.5);

    public GrapplingHook() {
        super("grappling-hook", "Grappling Hook", "Reels player toward fishing hook bobbers at high velocity", Category.MOVEMENT);
        addSettings(pullSpeed, slackDistance);
    }

    public double getPullSpeed() { return pullSpeed.getValue(); }
    public double getSlackDistance() { return slackDistance.getValue(); }

    public double[] calculatePullVector(double px, double py, double pz, double hx, double hy, double hz) {
        if (!isEnabled()) return new double[]{0.0, 0.0, 0.0};
        double dx = hx - px;
        double dy = hy - py;
        double dz = hz - pz;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist <= getSlackDistance() || dist == 0.0) {
            return new double[]{0.0, 0.0, 0.0};
        }
        return new double[]{(dx / dist) * getPullSpeed(), (dy / dist) * getPullSpeed(), (dz / dist) * getPullSpeed()};
    }
}
