package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Weapon Charge Tracker — Visual and audio indicator tracking attack meter charge
 */
public class WeaponChargeTracker extends Module {
    private final BooleanSetting playSoundOnFullCharge = new BooleanSetting("PlaySoundOnFullCharge", "Soft ping when weapon is fully charged", true);
    private final BooleanSetting showProgressBar = new BooleanSetting("ShowProgressBar", "Render cooldown bar beneath reticle", true);

    public WeaponChargeTracker() {
        super("weapon-charge-tracker", "Weapon Charge Tracker", "Visual and audio indicator tracking attack meter charge", Category.COMBAT);
        addSettings(playSoundOnFullCharge, showProgressBar);
    }

    public boolean isPlaySoundOnFullCharge() {
        return playSoundOnFullCharge.getValue();
    }

    public boolean isShowProgressBar() {
        return showProgressBar.getValue();
    }

    public double calculateAttackCooldownProgress(long elapsedTicks, double attackSpeed) {
        return Math.min(1.0, (double) elapsedTicks / (20.0 / attackSpeed));
    }
}
