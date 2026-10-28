package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Damage Attribute Scaler — Evaluates net weapon base damage, Sharpness bonus, Strength, and Weakness
 */
public class DamageAttributeScaler extends Module {
    private final BooleanSetting displayExactHpValue = new BooleanSetting("DisplayExactHpValue", "Render net damage number on screen", true);
    private final BooleanSetting showArmorMitigation = new BooleanSetting("ShowArmorMitigation", "Account for target armor points in display", true);

    public DamageAttributeScaler() {
        super("damage-attribute-scaler", "Damage Attribute Scaler", "Evaluates net weapon base damage, Sharpness bonus, Strength, and Weakness", Category.COMBAT);
        addSettings(displayExactHpValue, showArmorMitigation);
    }

    public boolean isDisplayExactHpValue() {
        return displayExactHpValue.getValue();
    }

    public boolean isShowArmorMitigation() {
        return showArmorMitigation.getValue();
    }

    public double computeNetWeaponDamage(double baseDamage, int sharpnessLevel, int strengthLevel, int weaknessLevel) {
        double dmg = baseDamage;
        if (sharpnessLevel > 0) dmg += 0.5 * sharpnessLevel + 0.5;
        if (strengthLevel > 0) dmg += 3.0 * strengthLevel;
        if (weaknessLevel > 0) dmg = Math.max(0.0, dmg - 4.0 * weaknessLevel);
        return dmg;
    }
}
