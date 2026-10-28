package com.ballsclient.core.module.combat.aim;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Trigger Bot — Automatically dispatches attack commands the exact tick the crosshair intersects a valid enemy hitbox within attack range
 */
public class TriggerBot extends Module {
    private final NumberSetting attackRange = new NumberSetting("AttackRange", "Maximum attack reach range", 3.8, 2.0, 6.0, 0.1);
    private final NumberSetting clickDelayMs = new NumberSetting("ClickDelayMs", "Delay between attacks in milliseconds", 50.0, 0.0, 250.0, 5.0);
    private final BooleanSetting criticalOnly = new BooleanSetting("CriticalOnly", "Only trigger when falling for critical hit", false);
    private final BooleanSetting checkWeaponCooldown = new BooleanSetting("CheckWeaponCooldown", "Require 100% weapon recharge", true);

    public TriggerBot() {
        super("trigger-bot", "Trigger Bot", "Automatically dispatches attack commands the exact tick the crosshair intersects a valid enemy hitbox within attack range", Category.COMBAT);
        addSettings(attackRange, clickDelayMs, criticalOnly, checkWeaponCooldown);
    }

    public double getAttackRange() {
        return attackRange.getValue();
    }

    public double getClickDelayMs() {
        return clickDelayMs.getValue();
    }

    public boolean isCriticalOnly() {
        return criticalOnly.getValue();
    }

    public boolean isCheckWeaponCooldown() {
        return checkWeaponCooldown.getValue();
    }

    public boolean shouldTrigger(boolean crosshairIntersects, double distance, double maxRange, double weaponCharge, boolean isFalling, boolean criticalOnly, boolean checkCooldown) {
        if (!crosshairIntersects || distance > maxRange) return false;
        if (checkCooldown && weaponCharge < 0.95) return false;
        if (criticalOnly && !isFalling) return false;
        return true;
    }
}
