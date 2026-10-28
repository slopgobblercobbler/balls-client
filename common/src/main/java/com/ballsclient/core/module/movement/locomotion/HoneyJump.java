package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Overrides honey block jump impediment and wall sliding friction.
 */
public class HoneyJump extends Module {
    private final BooleanSetting allowJump = new BooleanSetting("AllowJump", "Allow full jump velocity when standing on honey blocks", true);
    private final NumberSetting slideSpeed = new NumberSetting("SlideSpeed", "Vertical slide descent speed along honey walls", 0.12, 0.05, 0.5, 0.01);

    public HoneyJump() {
        super("honey-jump", "Honey Jump", "Overrides honey block jump impediment and wall sliding friction", Category.MOVEMENT);
        addSettings(allowJump, slideSpeed);
    }

    public boolean isAllowJump() { return allowJump.getValue(); }
    public double getSlideSpeed() { return slideSpeed.getValue(); }

    public double getJumpImpulse() {
        return (isEnabled() && isAllowJump()) ? 0.42 : 0.0;
    }
}
