package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Compact standalone potion HUD with amplifier roman numerals and active timers.
 */
public class PotionHUD extends Module {
    private final BooleanSetting showAmplifier = new BooleanSetting("ShowAmplifier", "Display roman numeral amplifier (e.g. Strength II)", true);
    private final BooleanSetting showIcon = new BooleanSetting("ShowIcon", "Render potion status icon", true);
    private final ColorSetting textColor = new ColorSetting("TextColor", "Potion name and timer text color", 0xFFFFFFFF);

    public static class PotionDisplayEntry {
        public final String name;
        public final int level;
        public final int remainingSeconds;

        public PotionDisplayEntry(String name, int level, int remainingSeconds) {
            this.name = name;
            this.level = level;
            this.remainingSeconds = remainingSeconds;
        }

        public String getLevelRoman() {
            return switch (level) {
                case 1 -> "I";
                case 2 -> "II";
                case 3 -> "III";
                case 4 -> "IV";
                case 5 -> "V";
                default -> String.valueOf(level);
            };
        }

        public String formatTimer() {
            int m = remainingSeconds / 60;
            int s = remainingSeconds % 60;
            return String.format("%d:%02d", m, s);
        }
    }

    private final List<PotionDisplayEntry> activePotions = new ArrayList<>();

    public PotionHUD() {
        super("potion-hud", "Potion HUD", "Standalone potion timer and amplifier display", Category.RENDER);
        addSettings(showAmplifier, showIcon, textColor);
    }

    public synchronized void updatePotions(List<PotionDisplayEntry> potions) {
        this.activePotions.clear();
        if (potions != null) {
            this.activePotions.addAll(potions);
        }
    }

    public synchronized List<PotionDisplayEntry> getActivePotions() {
        return Collections.unmodifiableList(new ArrayList<>(activePotions));
    }

    public boolean shouldShowAmplifier() {
        return showAmplifier.getValue();
    }

    public boolean shouldShowIcon() {
        return showIcon.getValue();
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render potion entries
    }
}
