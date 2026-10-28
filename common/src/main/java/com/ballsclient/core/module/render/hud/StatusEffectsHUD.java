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
 * Visual indicators for active potion and status effects with duration countdowns.
 */
public class StatusEffectsHUD extends Module {
    private final BooleanSetting pulseWhenLow = new BooleanSetting("PulseWhenLow", "Pulsate effect icons when duration is under 10 seconds", true);
    private final BooleanSetting compactMode = new BooleanSetting("CompactMode", "Compact single-line layout", false);
    private final NumberSetting scale = new NumberSetting("Scale", "HUD rendering scale", 1.0, 0.5, 2.0, 0.1);

    public static class EffectEntry {
        public final String name;
        public final int amplifier;
        public final int durationTicks;

        public EffectEntry(String name, int amplifier, int durationTicks) {
            this.name = name;
            this.amplifier = amplifier;
            this.durationTicks = durationTicks;
        }

        public String formatDuration() {
            int totalSeconds = durationTicks / 20;
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }

        public boolean isExpiringSoon() {
            return durationTicks <= 200; // <= 10 seconds
        }
    }

    private final List<EffectEntry> activeEffects = new ArrayList<>();

    public StatusEffectsHUD() {
        super("status-effects-hud", "Status Effects HUD", "Displays active potion effects and countdown timers", Category.RENDER);
        addSettings(pulseWhenLow, compactMode, scale);
    }

    public synchronized void updateEffects(List<EffectEntry> effects) {
        activeEffects.clear();
        if (effects != null) {
            activeEffects.addAll(effects);
        }
    }

    public synchronized List<EffectEntry> getActiveEffects() {
        return Collections.unmodifiableList(new ArrayList<>(activeEffects));
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render active effects
    }
}
