package com.ballsclient.core.module.render.hud;

import com.ballsclient.core.event.Render2DEvent;
import com.ballsclient.core.event.Subscribe;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.ModeSetting;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Real-world and in-game day/night cycle clock HUD overlay.
 */
public class ClockHUD extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Time display format", "REAL_TIME", "REAL_TIME", "GAME_TIME", "BOTH");
    private final BooleanSetting format24h = new BooleanSetting("24Hour", "Use 24-hour military clock format", true);
    private final ColorSetting textColor = new ColorSetting("TextColor", "Clock text color", 0xFFFFFFFF);

    private static final DateTimeFormatter FORMAT_24 = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter FORMAT_12 = DateTimeFormatter.ofPattern("hh:mm:ss a");

    private long worldTimeTicks = 6000;

    public ClockHUD() {
        super("clock-hud", "Clock HUD", "Real-world and in-game day/night cycle time display", Category.RENDER);
        addSettings(mode, format24h, textColor);
    }

    public void setWorldTimeTicks(long ticks) {
        this.worldTimeTicks = ticks % 24000;
    }

    public String formatRealTime() {
        LocalTime now = LocalTime.now();
        return format24h.getValue() ? now.format(FORMAT_24) : now.format(FORMAT_12);
    }

    public String formatGameTime() {
        // Minecraft 0 ticks = 6:00 AM, 6000 = 12:00 PM (Noon), 18000 = Midnight
        long hours = (worldTimeTicks / 1000 + 6) % 24;
        long minutes = (worldTimeTicks % 1000) * 60 / 1000;
        if (format24h.getValue()) {
            return String.format("%02d:%02d [MC]", hours, minutes);
        } else {
            String ampm = hours >= 12 ? "PM" : "AM";
            long h12 = hours % 12 == 0 ? 12 : hours % 12;
            return String.format("%d:%02d %s [MC]", h12, minutes, ampm);
        }
    }

    public String formatDisplayText() {
        if (mode.is("REAL_TIME")) {
            return formatRealTime();
        } else if (mode.is("GAME_TIME")) {
            return formatGameTime();
        } else {
            return formatRealTime() + " | " + formatGameTime();
        }
    }

    @Subscribe
    public void onRender2D(Render2DEvent event) {
        // Render clock
    }
}
