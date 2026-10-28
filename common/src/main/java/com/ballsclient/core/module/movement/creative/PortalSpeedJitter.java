package com.ballsclient.core.module.movement.creative;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Preserves momentum through nether portals without velocity zeroing.
 */
public class PortalSpeedJitter extends Module {
    private final NumberSetting velocityPreservePct = new NumberSetting("VelocityPreservePct", "Percentage of traversal velocity preserved through portal", 80.0, 20.0, 100.0, 5.0);

    public PortalSpeedJitter() {
        super("portal-speed-jitter", "Portal Speed Jitter", "Preserves momentum through nether portals without velocity zeroing", Category.MOVEMENT);
        addSettings(velocityPreservePct);
    }

    public double getVelocityPreservePct() { return velocityPreservePct.getValue(); }

    public double restoreVelocity(double storedSpeed) {
        if (!isEnabled()) return storedSpeed;
        return storedSpeed * (getVelocityPreservePct() / 100.0);
    }
}
