package com.ballsclient.fabric263.gui.clickgui.component;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.setting.NumberSetting;
import com.ballsclient.fabric263.gui.clickgui.ClickGuiCanvas;
import net.minecraft.client.gui.Font;

import java.util.Locale;

/**
 * Interactive slider component for NumberSetting with smooth mouse dragging and numeric readout.
 */
public class SliderComponent extends SettingComponent {
    private final NumberSetting setting;
    private boolean dragging = false;
    private int lastX;
    private int lastY;
    private int lastWidth;

    public SliderComponent(NumberSetting setting) {
        this.setting = setting;
    }

    public NumberSetting getSetting() {
        return setting;
    }

    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public void render(ClickGuiCanvas context, Font font, int x, int y, int width, int mouseX, int mouseY) {
        this.lastX = x;
        this.lastY = y;
        this.lastWidth = width;

        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeight();
        if (hovered || dragging) {
            context.fill(x, y, x + width, y + getHeight(), 0x15FFFFFF);
        }

        // Top line: name and numeric readout
        String name = setting.getName();
        double val = setting.getValue() != null ? setting.getValue() : setting.getMin();
        String valStr;
        if (setting.getStep() >= 1.0 && Math.floor(val) == val) {
            valStr = String.valueOf((long) val);
        } else {
            valStr = String.format(Locale.ROOT, "%.2f", val);
        }

        int valW = font.width(valStr);
        int maxNameW = width - valW - 12;
        if (font.width(name) > maxNameW && maxNameW > 10) {
            name = font.plainSubstrByWidth(name, maxNameW - 6) + "…";
        }

        context.drawString(font, name, x + 4, y + 2, 0xFFCCCCCC, false);
        context.drawString(font, valStr, x + width - valW - 4, y + 2, 0xFF00E5FF, false);

        // Bottom track & fill bar
        int barX = x + 4;
        int barY = y + 13;
        int barW = width - 8;
        int barH = 4;

        // Track background
        context.fill(barX, barY, barX + barW, barY + barH, 0xFF232530);

        // Active fill
        double min = setting.getMin();
        double max = setting.getMax();
        double ratio = (max > min) ? Math.max(0.0, Math.min(1.0, (val - min) / (max - min))) : 0.0;
        int fillW = (int) Math.round(barW * ratio);

        if (fillW > 0) {
            context.fill(barX, barY, barX + fillW, barY + barH, 0xFF5865F2);
        }

        // Knob
        int knobX = barX + fillW;
        context.fill(knobX - 1, barY - 1, knobX + 1, barY + barH + 1, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (button == 0 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeight()) {
            this.dragging = true;
            updateValueFromMouse(mouseX, x, width);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            if (BallsClientCore.getModuleManager() != null) {
                BallsClientCore.getModuleManager().saveConfig();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            updateValueFromMouse(mouseX, lastX, lastWidth);
            return true;
        }
        return false;
    }

    private void updateValueFromMouse(double mouseX, int x, int width) {
        int barX = x + 4;
        int barW = width - 8;
        if (barW <= 0) return;

        double ratio = Math.max(0.0, Math.min(1.0, (mouseX - barX) / (double) barW));
        double min = setting.getMin();
        double max = setting.getMax();
        double step = setting.getStep() > 0 ? setting.getStep() : 0.1;

        double rawVal = min + ratio * (max - min);
        double stepped = Math.round((rawVal - min) / step) * step + min;
        stepped = Math.max(min, Math.min(max, stepped));

        setting.setValue(stepped);
    }
}
