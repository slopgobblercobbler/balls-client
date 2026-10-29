package com.ballsclient.fabric263.gui.clickgui.component;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.fabric263.gui.clickgui.ClickGuiCanvas;
import net.minecraft.client.gui.Font;

import java.util.List;

/**
 * Interactive component for ModeSetting, cycling modes on click with responsive visual pill.
 */
public class DropdownComponent extends SettingComponent {
    private final ModeSetting setting;

    public DropdownComponent(ModeSetting setting) {
        this.setting = setting;
    }

    public ModeSetting getSetting() {
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
        String mode = setting.getValue() != null ? setting.getValue() : "";

        int modeW = font.width(mode);
        int maxNameW = width - modeW - 16;
        if (font.width(name) > maxNameW && maxNameW > 10) {
            name = font.plainSubstrByWidth(name, maxNameW - 6) + "…";
        }

        context.drawString(font, name, x + 4, y + 5, 0xFFCCCCCC, false);

        // Mode badge pill
        int pillX = x + width - modeW - 8;
        int pillY = y + 2;
        int pillH = 13;

        // Pill background (#22242F) with border (#3A3D4D)
        context.fill(pillX, pillY, x + width - 3, pillY + pillH, 0xFF22242F);
        context.renderOutline(pillX, pillY, (x + width - 3) - pillX, pillH, 0xFF3A3D4D);

        // Mode text
        context.drawString(font, mode, pillX + 3, pillY + 3, 0xFFFFAA00, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeight()) {
            List<String> modes = setting.getModes();
            if (modes != null && !modes.isEmpty()) {
                if (button == 0) {
                    // Left click: cycle forward
                    setting.cycle();
                } else if (button == 1) {
                    // Right click: cycle backward
                    int curIdx = modes.indexOf(setting.getValue());
                    if (curIdx <= 0) {
                        setting.setValue(modes.get(modes.size() - 1));
                    } else {
                        setting.setValue(modes.get(curIdx - 1));
                    }
                }
                if (BallsClientCore.getModuleManager() != null) {
                    BallsClientCore.getModuleManager().saveConfig();
                }
                return true;
            }
        }
        return false;
    }
}
