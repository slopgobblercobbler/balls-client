package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * TurtleEggProtector — Monitors beach turtle eggs and targets zombies stepping within trampling radius.
 */
public class TurtleEggProtector extends Module {
    private final NumberSetting defenseRadius = new NumberSetting("DefenseRadius", "Zone radius around turtle eggs to protect", 6.0, 2.0, 12.0, 1.0);
    private final BooleanSetting alertEggDamage = new BooleanSetting("AlertEggDamage", "Sound warning when egg cracking state increments", true);

    public TurtleEggProtector() {
        super("turtle-egg-protector", "Turtle Egg Protector", "Monitors beach turtle eggs and targets zombies stepping within trampling radius", Category.PLAYER);
        addSettings(defenseRadius, alertEggDamage);
    }

    public NumberSetting getDefenseRadius() {
        return defenseRadius;
    }

    public BooleanSetting getAlertEggDamage() {
        return alertEggDamage;
    }

    public static boolean isTrampleRisk(String mobType, double mobDist, double defenseRadius) {
        if (mobType == null) return false;
        String typeLower = mobType.toLowerCase();
        boolean isZombieVariant = typeLower.contains("zombie") || typeLower.contains("husk") || typeLower.contains("drowned") || typeLower.contains("zombified_piglin");
        return isZombieVariant && mobDist <= defenseRadius;
    }
}
