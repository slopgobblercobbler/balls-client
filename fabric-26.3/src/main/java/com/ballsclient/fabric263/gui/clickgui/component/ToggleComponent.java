package com.ballsclient.fabric263.gui.clickgui.component;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.fabric263.gui.clickgui.ClickGuiCanvas;
import net.minecraft.client.gui.Font;

/**
 * Clickable toggle switch component for BooleanSetting.
 */
public class ToggleComponent extends SettingComponent {
    private final BooleanSetting setting;

    public ToggleComponent(BooleanSetting setting) {
        this.setting = setting;
    }

    public BooleanSetting getSetting() {
        return setting;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void render(ClickGuiCanvas context, Font font, int x, int y, int width, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeight();
        if (hovered) {
            context.fill(x, y, x + width, y + getHeight(), 0x15FFFFFF);
        }

        // Setting name truncated if necessary
        String name = setting.getName();
        int maxTextW = width - 28;
        if (font.width(name) > maxTextW) {
            name = font.plainSubstrByWidth(name, maxTextW - 6) + "…";
        }
        context.drawString(font, name, x + 4, y + 4, 0xFFCCCCCC, false);

        // Toggle Switch / Pill
        int pillW = 18;
        int pillH = 9;
        int pillX = x + width - pillW - 4;
        int pillY = y + 4;

        boolean val = Boolean.TRUE.equals(setting.getValue());
        if (val) {
            // Active background (#43B581 - emerald green)
            context.fill(pillX, pillY, pillX + pillW, pillY + pillH, 0xFF43B581);
            // Knob on right
            context.fill(pillX + pillW - 8, pillY + 1, pillX + pillW - 1, pillY + pillH - 1, 0xFFFFFFFF);
        } else {
            // Inactive background (#2E3038 - dark slate)
            context.fill(pillX, pillY, pillX + pillW, pillY + pillH, 0xFF2E3038);
            // Knob on left
            context.fill(pillX + 1, pillY + 1, pillX + 8, pillY + pillH - 1, 0xFF72767D);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (button == 0 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeight()) {
            setting.toggle();
            if (BallsClientCore.getModuleManager() != null) {
                BallsClientCore.getModuleManager().saveConfig();
            }
            return true;
        }
        return false;
    }
}
