package com.ballsclient.core.module.movement.locomotion;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * Holds sneak stance client-side or packet-side without key hold.
 */
public class Sneak extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Sneaking transmission mode", "Vanilla", "Vanilla", "Packet", "Strict");
    private final BooleanSetting fullHeightHitbox = new BooleanSetting("FullHeightHitbox", "Retain full standing eye height during sneak", false);

    public Sneak() {
        super("sneak", "Sneak", "Holds sneak stance client-side or packet-side without key hold", Category.MOVEMENT);
        addSettings(mode, fullHeightHitbox);
    }

    public String getMode() { return mode.getValue(); }
    public boolean isFullHeightHitbox() { return fullHeightHitbox.getValue(); }

    public boolean shouldSneak() {
        return isEnabled();
    }
}
