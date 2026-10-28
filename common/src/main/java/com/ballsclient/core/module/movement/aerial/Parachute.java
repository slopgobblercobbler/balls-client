package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Deploys terminal velocity slow-fall when exceeding fall safety limit.
 */
public class Parachute extends Module {
    private final NumberSetting triggerFallDistance = new NumberSetting("TriggerFallDistance", "Fall distance in blocks before parachute deploys", 5.0, 2.0, 20.0, 0.5);
    private final NumberSetting terminalVelocity = new NumberSetting("TerminalVelocity", "Terminal vertical speed clamp while parachute is open", -0.15, -0.5, -0.05, 0.01);

    public Parachute() {
        super("parachute", "Parachute", "Deploys terminal velocity slow-fall when exceeding fall safety limit", Category.MOVEMENT);
        addSettings(triggerFallDistance, terminalVelocity);
    }

    public double getTriggerFallDistance() { return triggerFallDistance.getValue(); }
    public double getTerminalVelocity() { return terminalVelocity.getValue(); }

    public double applyParachute(double fallDistance, double currentVy) {
        if (!isEnabled()) return currentVy;
        if (fallDistance >= getTriggerFallDistance() && currentVy < getTerminalVelocity()) {
            return getTerminalVelocity();
        }
        return currentVy;
    }
}
