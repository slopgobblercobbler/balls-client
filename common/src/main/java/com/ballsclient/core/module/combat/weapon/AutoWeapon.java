package com.ballsclient.core.module.combat.weapon;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Auto Weapon — Dynamically equips best hotbar weapon for current target
 */
public class AutoWeapon extends Module {
    private final ModeSetting priorityWeapon = new ModeSetting("PriorityWeapon", "Preferred weapon priority mode", "HighestDPS", "HighestDPS", "Sword", "Axe", "Mace");
    private final BooleanSetting preferAxeAgainstShields = new BooleanSetting("PreferAxeAgainstShields", "Auto-equip axe when opponent blocks", true);

    public AutoWeapon() {
        super("auto-weapon", "Auto Weapon", "Dynamically equips best hotbar weapon for current target", Category.COMBAT);
        addSettings(priorityWeapon, preferAxeAgainstShields);
    }

    public String getPriorityWeapon() {
        return priorityWeapon.getValue();
    }

    public boolean isPreferAxeAgainstShields() {
        return preferAxeAgainstShields.getValue();
    }

    public int selectOptimalWeaponSlot(double[] damages, boolean[] isAxe, boolean enemyShieldUp) {
        if (enemyShieldUp) {
            for (int i = 0; i < isAxe.length; i++) {
                if (isAxe[i]) return i;
            }
        }
        int bestSlot = 0;
        double maxDmg = -1;
        for (int i = 0; i < damages.length; i++) {
            if (damages[i] > maxDmg) {
                maxDmg = damages[i];
                bestSlot = i;
            }
        }
        return bestSlot;
    }
}
