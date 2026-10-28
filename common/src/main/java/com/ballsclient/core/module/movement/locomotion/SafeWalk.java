package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Clamps horizontal velocity at block edges to prevent falling off.
 */
public class SafeWalk extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Edge safety surface scope", "GroundOnly", "GroundOnly", "AllSurfaces", "ShiftKey");
    private final NumberSetting edgeOffset = new NumberSetting("EdgeOffset", "Safety margin from block edge", 0.05, 0.0, 0.30, 0.01);
    private final BooleanSetting lookDownOnly = new BooleanSetting("LookDownOnly", "Only apply safety clamping when looking downward", false);

    public SafeWalk() {
        super("safe-walk", "Safe Walk", "Clamps horizontal velocity at block edges to prevent falling off", Category.MOVEMENT);
        addSettings(mode, edgeOffset, lookDownOnly);
    }

    public String getMode() { return mode.getValue(); }
    public double getEdgeOffset() { return edgeOffset.getValue(); }
    public boolean isLookDownOnly() { return lookDownOnly.getValue(); }

    public double clampMotion(double currentX, double deltaX, double blockMaxX, boolean onGround) {
        if (!isEnabled()) return currentX + deltaX;
        if (!onGround && "GroundOnly".equalsIgnoreCase(getMode())) return currentX + deltaX;
        double target = currentX + deltaX;
        double boundary = blockMaxX - getEdgeOffset();
        if (deltaX > 0 && target > boundary) {
            return boundary;
        }
        return target;
    }
}
