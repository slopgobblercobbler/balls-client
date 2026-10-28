package com.ballsclient.core.module.player.interaction;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * ItemFrameLocker — Protects item frames from accidental click rotation or left-click destruction.
 */
public class ItemFrameLocker extends Module {
    private final BooleanSetting preventRotation = new BooleanSetting("PreventRotation", "Block right clicks rotating item frames", true);
    private final BooleanSetting preventBreak = new BooleanSetting("PreventBreak", "Block left clicks breaking item frames", true);
    private final BooleanSetting allowCrouchBypass = new BooleanSetting("AllowCrouchBypass", "Allow interaction when sneaking", true);

    public ItemFrameLocker() {
        super("item-frame-locker", "Item Frame Locker", "Protects item frames from accidental click rotation or left-click destruction", Category.PLAYER);
        addSettings(preventRotation, preventBreak, allowCrouchBypass);
    }

    public BooleanSetting getPreventRotation() {
        return preventRotation;
    }

    public BooleanSetting getPreventBreak() {
        return preventBreak;
    }

    public BooleanSetting getAllowCrouchBypass() {
        return allowCrouchBypass;
    }

    public static boolean shouldCancelFrameAction(boolean isSneaking, boolean allowBypass) {
        if (isSneaking && allowBypass) {
            return false;
        }
        return true;
    }
}
