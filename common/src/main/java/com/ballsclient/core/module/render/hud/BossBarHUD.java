package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.NumberSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom boss health bar stacking and layout tweaker with percentage indicators.
 */
public class BossBarHUD extends Module {
    private final BooleanSetting compact = new BooleanSetting("Compact", "Stack bars vertically into ultra-compact rows", false);
    private final BooleanSetting showPercentage = new BooleanSetting("ShowPercentage", "Display exact percentage text over bar", true);
    private final NumberSetting scale = new NumberSetting("Scale", "Boss bar rendering scale", 1.0, 0.5, 2.0, 0.1);

    public static class BossBarEntry {
        public final String name;
        public final float percent;
        public final int colorRgb;

        public BossBarEntry(String name, float percent, int colorRgb) {
            this.name = name;
            this.percent = Math.min(1.0f, Math.max(0.0f, percent));
            this.colorRgb = colorRgb;
        }

        public String getPercentString() {
            return String.format("%.1f%%", percent * 100.0f);
        }
    }

    private final List<BossBarEntry> activeBars = new ArrayList<>();

    public BossBarHUD() {
        super("boss-bar-hud", "Boss Bar HUD", "Compact stacking and custom percentage readouts for boss health bars", Category.RENDER);
        addSettings(compact, showPercentage, scale);
    }

    public synchronized void updateBossBars(List<BossBarEntry> bars) {
        this.activeBars.clear();
        if (bars != null) {
            this.activeBars.addAll(bars);
        }
    }

    public synchronized List<BossBarEntry> getActiveBars() {
        return Collections.unmodifiableList(new ArrayList<>(activeBars));
    }

    public boolean isCompact() {
        return compact.getValue();
    }

    public boolean shouldShowPercentage() {
        return showPercentage.getValue();
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render boss bars
    }
}
