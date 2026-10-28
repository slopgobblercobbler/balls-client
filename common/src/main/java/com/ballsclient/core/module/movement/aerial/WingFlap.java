package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Rhythmic impulse pulses during flight simulating bird wing flapping.
 */
public class WingFlap extends Module {
    private final NumberSetting flapInterval = new NumberSetting("FlapInterval", "Ticks between consecutive wing flap impulses", 12.0, 4.0, 40.0, 1.0);
    private final NumberSetting impulse = new NumberSetting("Impulse", "Vertical lift impulse per wing flap", 0.38, 0.1, 1.0, 0.02);

    public WingFlap() {
        super("wing-flap", "Wing Flap", "Rhythmic impulse pulses during flight simulating bird wing flapping", Category.MOVEMENT);
        addSettings(flapInterval, impulse);
    }

    public double getFlapInterval() { return flapInterval.getValue(); }
    public double getImpulse() { return impulse.getValue(); }

    public boolean shouldFlap(int airborneTicks) {
        return isEnabled() && airborneTicks > 0 && airborneTicks % (int) getFlapInterval() == 0;
    }

    public double getFlapImpulse() {
        return isEnabled() ? getImpulse() : 0.0;
    }
}
