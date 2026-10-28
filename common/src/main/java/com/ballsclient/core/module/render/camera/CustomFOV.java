package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Extends FOV options beyond vanilla limits (from 30 up to 160 degrees) with sprinting FOV dampening.
 */
public class CustomFOV extends Module {
    private final NumberSetting fov = new NumberSetting("FOV", "Custom field of view in degrees", 90.0, 30.0, 160.0, 1.0);
    private final BooleanSetting sprintFovModifier = new BooleanSetting("SprintModifier", "Apply vanilla FOV zoom during sprinting", false);

    public CustomFOV() {
        super("custom-fov", "Custom FOV", "Custom field-of-view configuration extending beyond vanilla limits", Category.RENDER);
        addSettings(fov, sprintFovModifier);
    }

    public double getFov() { return fov.getValue(); }
    public boolean isSprintFovModifier() { return sprintFovModifier.getValue(); }
}
