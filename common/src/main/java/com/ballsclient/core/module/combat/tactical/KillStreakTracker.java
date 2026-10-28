package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Kill Streak Tracker — Logs consecutive player eliminations with combat alerts and session telemetry
 */
public class KillStreakTracker extends Module {
    private final BooleanSetting announceKillStreaks = new BooleanSetting("AnnounceKillStreaks", "Announce streaks in chat", true);
    private final BooleanSetting resetStreakOnDeath = new BooleanSetting("ResetStreakOnDeath", "Reset streak counter to 0 on death", true);

    public KillStreakTracker() {
        super("kill-streak-tracker", "Kill Streak Tracker", "Logs consecutive player eliminations with combat alerts and session telemetry", Category.COMBAT);
        addSettings(announceKillStreaks, resetStreakOnDeath);
    }

    public boolean isAnnounceKillStreaks() {
        return announceKillStreaks.getValue();
    }

    public boolean isResetStreakOnDeath() {
        return resetStreakOnDeath.getValue();
    }

    public int recordKill(int currentStreak) {
        return currentStreak + 1;
    }
}
