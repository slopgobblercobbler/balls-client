package com.ballsclient.core.module.movement.aerial;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Cancels or reverses Shulker levitation potion effect.
 */
public class AntiLevitation extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Levitation countermeasure action", "Cancel", "Cancel", "Slow", "Reverse");
    private final NumberSetting downwardSpeed = new NumberSetting("DownwardSpeed", "Descent speed when in Reverse mode", 0.10, 0.0, 0.5, 0.02);

    public AntiLevitation() {
        super("anti-levitation", "Anti Levitation", "Cancels or reverses Shulker levitation potion effect", Category.MOVEMENT);
        addSettings(mode, downwardSpeed);
    }

    public String getMode() { return mode.getValue(); }
    public double getDownwardSpeed() { return downwardSpeed.getValue(); }

    public double modifyLevitationVelocity(double vy) {
        if (!isEnabled()) return vy;
        if ("Cancel".equalsIgnoreCase(getMode())) return 0.0;
        if ("Reverse".equalsIgnoreCase(getMode())) return -getDownwardSpeed();
        if ("Slow".equalsIgnoreCase(getMode())) return vy * 0.2;
        return vy;
    }
}
