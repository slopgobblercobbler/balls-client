package com.ballsclient.core.module.combat.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Weapon Reload — Automatically reloads discharged hotbar crossbows during combat pauses
 */
public class AutoWeaponReload extends Module {
    private final NumberSetting reloadPauseThresholdMs = new NumberSetting("ReloadPauseThresholdMs", "Idle pause duration before commencing crossbow reload", 1500.0, 500.0, 3000.0, 50.0);
    private final BooleanSetting notifyReloadComplete = new BooleanSetting("NotifyReloadComplete", "Soft audio tone when all crossbows ready", true);

    public AutoWeaponReload() {
        super("auto-weapon-reload", "Auto Weapon Reload", "Automatically reloads discharged hotbar crossbows during combat pauses", Category.COMBAT);
        addSettings(reloadPauseThresholdMs, notifyReloadComplete);
    }

    public double getReloadPauseThresholdMs() {
        return reloadPauseThresholdMs.getValue();
    }

    public boolean isNotifyReloadComplete() {
        return notifyReloadComplete.getValue();
    }

    public boolean shouldStartReload(boolean isHoldingCrossbow, boolean isCharged, long msSinceLastCombatAction, long thresholdMs) {
        return isHoldingCrossbow && !isCharged && msSinceLastCombatAction >= thresholdMs;
    }
}
