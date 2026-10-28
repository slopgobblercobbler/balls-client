package com.ballsclient.core.module.automation.farming;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * AnimalBreederAutomation — Automates holding/feeding breeding food to adult passive mobs while tracking love timers.
 */
public class AnimalBreederAutomation extends Module {
    private final NumberSetting loveCooldownMinutes = new NumberSetting("LoveCooldownMinutes", "Minimum breeding recovery cooldown in minutes", 5.0, 1.0, 10.0, 1.0);
    private final BooleanSetting ignoreBabies = new BooleanSetting("IgnoreBabies", "Skips juvenile animals incapable of reproduction", true);

    public AnimalBreederAutomation() {
        super("animal-breeder-automation", "Animal Breeder Automation", "Automates holding/feeding breeding food to adult passive mobs while tracking love timers", Category.AUTOMATION);
        addSettings(loveCooldownMinutes, ignoreBabies);
    }

    public NumberSetting getLoveCooldownMinutes() {
        return loveCooldownMinutes;
    }

    public BooleanSetting getIgnoreBabies() {
        return ignoreBabies;
    }

    public boolean canFeed(boolean isBaby, boolean isInLove, long lastBredTimestamp, long currentTimestamp) {
        if (ignoreBabies.getValue() && isBaby) {
            return false;
        }
        if (isInLove) {
            return false;
        }
        long cooldownMs = (long) (loveCooldownMinutes.getValue() * 60.0 * 1000.0);
        return currentTimestamp - lastBredTimestamp >= cooldownMs;
    }
}
