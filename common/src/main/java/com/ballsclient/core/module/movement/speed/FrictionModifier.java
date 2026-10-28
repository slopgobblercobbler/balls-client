package com.ballsclient.core.module.movement.speed;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Fine-tunes global ground and air friction damping factors.
 */
public class FrictionModifier extends Module {
    private final NumberSetting groundFriction = new NumberSetting("GroundFriction", "Damping multiplier applied per tick on ground", 0.60, 0.10, 1.0, 0.02);
    private final NumberSetting airFriction = new NumberSetting("AirFriction", "Damping multiplier applied per tick mid-air", 0.91, 0.50, 1.0, 0.01);

    public FrictionModifier() {
        super("friction-modifier", "Friction Modifier", "Fine-tunes global ground and air friction damping factors", Category.MOVEMENT);
        addSettings(groundFriction, airFriction);
    }

    public double getGroundFriction() { return groundFriction.getValue(); }
    public double getAirFriction() { return airFriction.getValue(); }

    public double applyFriction(double v, boolean onGround) {
        if (!isEnabled()) return v;
        return v * (onGround ? getGroundFriction() : getAirFriction());
    }
}
