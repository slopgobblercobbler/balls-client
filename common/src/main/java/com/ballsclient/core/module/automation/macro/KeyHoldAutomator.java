package com.ballsclient.core.module.automation.macro;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;

/**
 * KeyHoldAutomator — Keeps designated keys continuously depressed (Left Click, Right Click, W) with toggle hotkey.
 */
public class KeyHoldAutomator extends Module {
    private final BooleanSetting holdAttack = new BooleanSetting("HoldAttack", "Continuously depresses Left Click / Attack", false);
    private final BooleanSetting holdUse = new BooleanSetting("HoldUse", "Continuously depresses Right Click / Use", false);
    private final BooleanSetting holdForward = new BooleanSetting("HoldForward", "Continuously depresses Forward / W", false);

    public KeyHoldAutomator() {
        super("key-hold-automator", "Key Hold Automator", "Keeps designated keys continuously depressed (Left Click, Right Click, W) with toggle hotkey", Category.AUTOMATION);
        addSettings(holdAttack, holdUse, holdForward);
    }

    public BooleanSetting getHoldAttack() {
        return holdAttack;
    }

    public BooleanSetting getHoldUse() {
        return holdUse;
    }

    public BooleanSetting getHoldForward() {
        return holdForward;
    }
}
