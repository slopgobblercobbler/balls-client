package com.ballsclient.core.module.combat.tactical;

import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;

/**
 * Target Stats Overlay — HUD overlay displaying opponent active potion effects, absorption HP, and ping
 */
public class TargetStatsOverlay extends Module {
    private final BooleanSetting showPotionEffects = new BooleanSetting("ShowPotionEffects", "Display status effect icons", true);
    private final BooleanSetting showPing = new BooleanSetting("ShowPing", "Display target round-trip latency", true);

    public TargetStatsOverlay() {
        super("target-stats-overlay", "Target Stats Overlay", "HUD overlay displaying opponent active potion effects, absorption HP, and ping", Category.COMBAT);
        addSettings(showPotionEffects, showPing);
    }

    public boolean isShowPotionEffects() {
        return showPotionEffects.getValue();
    }

    public boolean isShowPing() {
        return showPing.getValue();
    }

    public String formatTargetHpString(double health, double absorption) {
        return String.format("%.1f (+%.1f)", health, absorption);
    }
}
