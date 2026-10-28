package com.ballsclient.core.module.player.automation;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;

/**
 * PotionBrewerScheduler — Automates ingredient sequencing (Nether Wart -> Secondary -> Redstone) in stands.
 */
public class PotionBrewerScheduler extends Module {
    private final ModeSetting targetBrew = new ModeSetting("TargetBrew", "Target potion recipe", "SpeedII", "SpeedII", "FireResistance", "Invisibility", "HealingII", "WaterBreathing");
    private final BooleanSetting autoBlazePowder = new BooleanSetting("AutoBlazePowder", "Refill blaze powder fuel slot automatically", true);

    public PotionBrewerScheduler() {
        super("potion-brewer-scheduler", "Potion Brewer Scheduler", "Automates ingredient sequencing (Nether Wart -> Secondary -> Redstone) in stands", Category.PLAYER);
        addSettings(targetBrew, autoBlazePowder);
    }

    public ModeSetting getTargetBrew() {
        return targetBrew;
    }

    public BooleanSetting getAutoBlazePowder() {
        return autoBlazePowder;
    }

    public static String getStageIngredient(String targetBrew, int stage) {
        if (stage == 0) return "nether_wart";
        if ("SpeedII".equalsIgnoreCase(targetBrew)) {
            if (stage == 1) return "sugar";
            if (stage == 2) return "glowstone_dust";
        } else if ("FireResistance".equalsIgnoreCase(targetBrew)) {
            if (stage == 1) return "magma_cream";
            if (stage == 2) return "redstone";
        } else if ("Invisibility".equalsIgnoreCase(targetBrew)) {
            if (stage == 1) return "golden_carrot";
            if (stage == 2) return "fermented_spider_eye";
        } else if ("HealingII".equalsIgnoreCase(targetBrew)) {
            if (stage == 1) return "glistering_melon_slice";
            if (stage == 2) return "glowstone_dust";
        } else if ("WaterBreathing".equalsIgnoreCase(targetBrew)) {
            if (stage == 1) return "pufferfish";
            if (stage == 2) return "redstone";
        }
        return "gunpowder";
    }
}
