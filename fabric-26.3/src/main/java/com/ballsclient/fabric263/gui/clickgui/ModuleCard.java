package com.ballsclient.fabric263.gui.clickgui;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ColorSetting;
import com.ballsclient.core.setting.KeybindSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;
import com.ballsclient.core.setting.Setting;
import com.ballsclient.fabric263.gui.clickgui.component.ColorPickerComponent;
import com.ballsclient.fabric263.gui.clickgui.component.DropdownComponent;
import com.ballsclient.fabric263.gui.clickgui.component.SettingComponent;
import com.ballsclient.fabric263.gui.clickgui.component.SliderComponent;
import com.ballsclient.fabric263.gui.clickgui.component.ToggleComponent;
import com.ballsclient.fabric263.gui.clickgui.ClickGuiCanvas;
import net.minecraft.client.gui.Font;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Visual card representing a module within a category panel.
 * Features inline toggling, per-module keybind rebinding listener, and expandable settings drawer.
 */
public class ModuleCard {
    public static final int HEADER_HEIGHT = 18;

    private final Module module;
    private final List<SettingComponent> settingComponents = new ArrayList<>();
    private boolean expanded = false;
    private boolean rebinding = false;

    private int lastX;
    private int lastY;
    private int lastWidth;

    public ModuleCard(Module module) {
        this.module = module;

        for (Setting<?> s : module.getSettings()) {
            if (s instanceof KeybindSetting) {
                // Handled directly by card's inline keybind button
                continue;
            }
            if (s instanceof BooleanSetting b) {
                settingComponents.add(new ToggleComponent(b));
            } else if (s instanceof NumberSetting n) {
                settingComponents.add(new SliderComponent(n));
            } else if (s instanceof ModeSetting m) {
                settingComponents.add(new DropdownComponent(m));
            } else if (s instanceof ColorSetting c) {
                settingComponents.add(new ColorPickerComponent(c));
            }
        }
    }

    public Module getModule() {
        return module;
    }

    public boolean isRebinding() {
        return rebinding;
    }

    public void setRebinding(boolean rebinding) {
        this.rebinding = rebinding;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getHeight() {
        if (!expanded || settingComponents.isEmpty()) {
            return HEADER_HEIGHT;
        }
        int h = HEADER_HEIGHT + 2;
        for (SettingComponent comp : settingComponents) {
            h += comp.getHeight();
        }
        return h + 2;
    }

    public void render(ClickGuiCanvas context, Font font, int x, int y, int width, int mouseX, int mouseY, BiConsumer<String, String> tooltipConsumer) {
        this.lastX = x;
        this.lastY = y;
        this.lastWidth = width;

        boolean enabled = module.isEnabled();
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + HEADER_HEIGHT;

        // Card header background
        int bg = enabled ? 0xDD1B2333 : 0xDD14151B;
        if (hovered) {
            bg = enabled ? 0xEE222E42 : 0xEE1B1C24;
        }
        context.fill(x, y, x + width, y + HEADER_HEIGHT, bg);

        // Left accent stripe for enabled state
        if (enabled) {
            context.fill(x, y, x + 2, y + HEADER_HEIGHT, 0xFF43B581); // Emerald Green
        }

        // Module name formatting
        String name = module.getName();
        int textColor = enabled ? 0xFFFFFFFF : 0xFFA0A2B0;

        // Keybind button layout
        String keyStr = rebinding ? "[Press key...]" : "[" + module.getKeybind().getDisplayName() + "]";
        int keyW = font.width(keyStr) + 4;
        int chevronW = settingComponents.isEmpty() ? 0 : 10;
        int keyX = x + width - chevronW - keyW - 4;
        int keyY = y + 3;
        int keyH = 12;

        int maxNameW = (keyX - x) - 8;
        if (font.width(name) > maxNameW && maxNameW > 10) {
            name = font.plainSubstrByWidth(name, maxNameW - 6) + "…";
        }
        context.drawString(font, name, x + 6, y + 5, textColor, false);

        // Render keybind button
        int keyBg = rebinding ? 0xFF5865F2 : 0xFF22242D;
        int keyBorder = rebinding ? 0xFF7289DA : 0xFF353744;
        int keyText = rebinding ? 0xFFFFFFFF : (module.getKeybind().isBound() ? 0xFF58A6FF : 0xFF72767D);

        context.fill(keyX, keyY, keyX + keyW, keyY + keyH, keyBg);
        context.renderOutline(keyX, keyY, keyW, keyH, keyBorder);
        context.drawString(font, keyStr, keyX + 2, keyY + 2, keyText, false);

        // Settings chevron (if module has settings)
        if (!settingComponents.isEmpty()) {
            String chevron = expanded ? "▼" : "▶";
            int chevX = x + width - 10;
            context.drawString(font, chevron, chevX, y + 5, 0xFF72767D, false);
        }

        // Hover tooltip detection
        if (hovered && tooltipConsumer != null) {
            tooltipConsumer.accept(module.getName(), module.getDescription());
        }

        // Expandable settings drawer
        if (expanded && !settingComponents.isEmpty()) {
            int drawerY = y + HEADER_HEIGHT;
            int drawerH = getHeight() - HEADER_HEIGHT;

            // Inset darker background (#101116)
            context.fill(x, drawerY, x + width, drawerY + drawerH, 0xF0101116);
            // Inset left border (#2F313C)
            context.fill(x, drawerY, x + 2, drawerY + drawerH, 0xFF2F313C);

            int compY = drawerY + 2;
            for (SettingComponent comp : settingComponents) {
                comp.render(context, font, x + 4, compY, width - 6, mouseX, mouseY);
                compY += comp.getHeight();
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX < lastX || mouseX > lastX + lastWidth) {
            return false;
        }

        // Check header click
        if (mouseY >= lastY && mouseY < lastY + HEADER_HEIGHT) {
            // Keybind button bounds
            String keyStr = rebinding ? "[Press key...]" : "[" + module.getKeybind().getDisplayName() + "]";
            int fontW = 6 * keyStr.length(); // approximate safe fallback
            try {
                // If font available, precise; otherwise bounding
                fontW = net.minecraft.client.Minecraft.getInstance().font.width(keyStr);
            } catch (Throwable ignored) {}
            int keyW = fontW + 4;
            int chevronW = settingComponents.isEmpty() ? 0 : 10;
            int keyX = lastX + lastWidth - chevronW - keyW - 4;

            if (button == 0 && mouseX >= keyX && mouseX <= keyX + keyW) {
                // Clicked keybind button -> enter rebinding mode
                this.rebinding = true;
                return true;
            }

            // Check chevron or right-click to expand settings
            if (button == 1 || (!settingComponents.isEmpty() && mouseX >= lastX + lastWidth - chevronW - 2)) {
                if (!settingComponents.isEmpty()) {
                    this.expanded = !this.expanded;
                    return true;
                }
            }

            // Clicked module body -> toggle module
            if (button == 0) {
                module.toggle();
                if (BallsClientCore.getModuleManager() != null) {
                    BallsClientCore.getModuleManager().saveConfig();
                }
                return true;
            }
        }

        // Check settings click if expanded
        if (expanded && mouseY >= lastY + HEADER_HEIGHT && mouseY < lastY + getHeight()) {
            int compY = lastY + HEADER_HEIGHT + 2;
            for (SettingComponent comp : settingComponents) {
                if (comp.mouseClicked(mouseX, mouseY, button, lastX + 4, compY, lastWidth - 6)) {
                    return true;
                }
                compY += comp.getHeight();
            }
        }

        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (expanded) {
            for (SettingComponent comp : settingComponents) {
                if (comp.mouseReleased(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (expanded) {
            for (SettingComponent comp : settingComponents) {
                if (comp.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Handles key capture during rebinding mode.
     * ESC, DELETE, or BACKSPACE unbinds to KEY_NONE (-1).
     * Any other key assigns keycode.
     */
    public boolean handleKey(int keyCode) {
        if (!rebinding) {
            return false;
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            module.getKeybind().setValue(KeybindSetting.KEY_NONE);
        } else {
            module.getKeybind().setValue(keyCode);
        }

        this.rebinding = false;
        if (BallsClientCore.getModuleManager() != null) {
            BallsClientCore.getModuleManager().saveConfig();
        }
        return true;
    }
}
