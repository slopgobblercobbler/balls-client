package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Dynamically scales and squishes entity models upon taking heavy damage for cartoony impact physics.
 */
public class PopUpDamageAnimation extends Module {
    private final NumberSetting squishFactor = new NumberSetting("SquishFactor", "Model compression factor on hit", 0.3, 0.1, 0.8, 0.05);
    private final NumberSetting durationTicks = new NumberSetting("DurationTicks", "Squish recovery duration in ticks", 8.0, 2.0, 20.0, 1.0);

    public PopUpDamageAnimation() {
        super("pop-up-damage-animation", "Pop-Up Damage Animation", "Cartoony squish and scale animation on damaged entity models", Category.RENDER);
        addSettings(squishFactor, durationTicks);
    }

    public double getSquishFactor() { return squishFactor.getValue(); }
    public int getDurationTicks() { return durationTicks.getIntValue(); }
}
