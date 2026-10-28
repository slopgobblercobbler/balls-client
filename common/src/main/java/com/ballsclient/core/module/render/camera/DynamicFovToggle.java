package com.ballsclient.core.module.render.camera;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * Granularly toggles dynamic FOV changes caused by sprinting, speed potions, and bow charging.
 */
public class DynamicFovToggle extends Module {
    private final BooleanSetting affectSprinting = new BooleanSetting("Sprinting", "Allow sprinting to widen FOV", false);
    private final BooleanSetting affectPotions = new BooleanSetting("Potions", "Allow Speed/Slowness to warp FOV", false);
    private final BooleanSetting affectBow = new BooleanSetting("Bow", "Allow bow draw zoom to alter FOV", true);

    public DynamicFovToggle() {
        super("dynamic-fov-toggle", "Dynamic FOV Toggle", "Controls dynamic FOV alterations from sprinting, potions, and bows", Category.RENDER);
        addSettings(affectSprinting, affectPotions, affectBow);
    }

    public boolean isAffectSprinting() { return affectSprinting.getValue(); }
    public boolean isAffectPotions() { return affectPotions.getValue(); }
    public boolean isAffectBow() { return affectBow.getValue(); }
}
