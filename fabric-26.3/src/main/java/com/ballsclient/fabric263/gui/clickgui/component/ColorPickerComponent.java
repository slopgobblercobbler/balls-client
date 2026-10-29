package com.ballsclient.fabric263.gui.clickgui.component;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.fabric263.gui.clickgui.ClickGuiCanvas;
import net.minecraft.client.gui.Font;

/**
 * Interactive color component for ColorSetting with chroma toggling and color preset cycling.
 */
public class ColorPickerComponent extends SettingComponent {
    private final ColorSetting setting;

    private static final int[] PRESETS = new int[]{
            0xFFFF4444, // Red
            0xFFFF8800, // Orange
            0xFFFFEE00, // Yellow
            0xFF44FF44, // Green
            0xFF00FFFF, // Cyan
            0xFF5865F2, // Blurple
            0xFFAA44FF, // Purple
            0xFFFF44AA, // Pink
            0xFFFFFFFF  // White
    };

    public ColorPickerComponent(ColorSetting setting) {
        this.setting = setting;
    }

    public ColorSetting getSetting() {
        return setting;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void render(ClickGuiCanvas context, Font font, int x, int y, int width, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeight();
        if (hovered) {
            context.fill(x, y, x + width, y + getHeight(), 0x15FFFFFF);
        }

        // Setting name
        String name = setting.getName();
        int maxNameW = width - 56;
        if (font.width(name) > maxNameW && maxNameW > 10) {
            name = font.plainSubstrByWidth(name, maxNameW - 6) + "…";
        }
        context.drawString(font, name, x + 4, y + 5, 0xFFCCCCCC, false);

        // Chroma toggle button
        int chrX = x + width - 48;
        int chrY = y + 3;
        int chrW = 24;
        int chrH = 12;

        boolean chroma = setting.isChroma();
        int chrBg = chroma ? 0xFF5865F2 : 0xFF242630;
        int chrBorder = chroma ? 0xFF7289DA : 0xFF3A3D4D;
        int chrText = chroma ? 0xFFFFFFFF : 0xFF888888;

        context.fill(chrX, chrY, chrX + chrW, chrY + chrH, chrBg);
        context.renderOutline(chrX, chrY, chrW, chrH, chrBorder);
        context.drawString(font, "CHR", chrX + 3, chrY + 2, chrText, false);

        // Color preview swatch
        int swatchX = x + width - 18;
        int swatchY = y + 3;
        int swatchW = 14;
        int swatchH = 12;

        int color = setting.getEffectiveRgba(System.currentTimeMillis());
        context.fill(swatchX, swatchY, swatchX + swatchW, swatchY + swatchH, color);
        context.renderOutline(swatchX, swatchY, swatchW, swatchH, 0xFF4A4D5E);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (button == 0 && mouseY >= y && mouseY <= y + getHeight()) {
            // Check Chroma button bounds
            int chrX = x + width - 48;
            int chrW = 24;
            if (mouseX >= chrX && mouseX <= chrX + chrW) {
                setting.setChroma(!setting.isChroma());
                if (BallsClientCore.getModuleManager() != null) {
                    BallsClientCore.getModuleManager().saveConfig();
                }
                return true;
            }

            // Check Swatch bounds
            int swatchX = x + width - 18;
            int swatchW = 14;
            if (mouseX >= swatchX && mouseX <= swatchX + swatchW) {
                int cur = setting.getValue();
                int next = PRESETS[0];
                for (int i = 0; i < PRESETS.length; i++) {
                    if (PRESETS[i] == cur) {
                        next = PRESETS[(i + 1) % PRESETS.length];
                        break;
                    }
                }
                setting.setValue(next);
                if (BallsClientCore.getModuleManager() != null) {
                    BallsClientCore.getModuleManager().saveConfig();
                }
                return true;
            }
        }
        return false;
    }
}
