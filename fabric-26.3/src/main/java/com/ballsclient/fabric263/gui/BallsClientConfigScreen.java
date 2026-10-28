package com.ballsclient.fabric263.gui;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class BallsClientConfigScreen extends Screen {
    private final Screen parent;
    private Category currentCategory = null; // null = ALL
    private String searchText = "";
    private EditBox searchBox;
    private int scrollY = 0;
    private Module settingsModule = null; // Module currently expanded for settings editing

    private final List<Module> displayModules = new ArrayList<>();
    private final List<CategoryButton> categoryButtons = new ArrayList<>();

    private record CategoryButton(Category category, String label, int x, int y, int width, int height) {}

    public BallsClientConfigScreen(Screen parent) {
        super(Component.literal("BallsClient Dashboard"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.categoryButtons.clear();

        // 1. Search Box at top-center (Y = 32)
        int searchWidth = 240;
        int searchX = this.width / 2 - searchWidth / 2;
        searchBox = new EditBox(this.font, searchX, 32, searchWidth, 18, Component.literal("Search..."));
        searchBox.setResponder(text -> {
            this.searchText = text != null ? text.trim().toLowerCase() : "";
            this.scrollY = 0;
            updateFilteredModules();
        });
        this.addRenderableWidget(searchBox);

        // 2. Done / Close Button at bottom
        int btnWidth = 120;
        this.addRenderableWidget(Button.builder(Component.literal("Save & Close"), b -> this.onClose())
                .bounds(this.width / 2 - btnWidth / 2, this.height - 26, btnWidth, 20)
                .build());

        // 3. Category Buttons across the top bar (Y = 8)
        Category[] cats = Category.values();
        int totalTabs = cats.length + 1; // +1 for "ALL"
        int pad = 4;
        int tabW = Math.max(48, (this.width - 20 - (totalTabs * pad)) / totalTabs);
        int totalWidth = totalTabs * (tabW + pad);
        int startX = Math.max(10, (this.width - totalWidth) / 2);

        // "ALL" Tab
        categoryButtons.add(new CategoryButton(null, "ALL", startX, 8, tabW, 18));
        for (int i = 0; i < cats.length; i++) {
            categoryButtons.add(new CategoryButton(cats[i], cats[i].name(), startX + (i + 1) * (tabW + pad), 8, tabW, 18));
        }

        updateFilteredModules();
    }

    private void updateFilteredModules() {
        displayModules.clear();
        if (BallsClientCore.getModuleManager() == null) {
            return;
        }

        Collection<Module> all = BallsClientCore.getModuleManager().getModules();
        for (Module m : all) {
            if (currentCategory != null && m.getCategory() != currentCategory) {
                continue;
            }
            if (!searchText.isEmpty()) {
                boolean idMatch = m.getId().toLowerCase().contains(searchText);
                boolean nameMatch = m.getName().toLowerCase().contains(searchText);
                boolean descMatch = m.getDescription() != null && m.getDescription().toLowerCase().contains(searchText);
                if (!idMatch && !nameMatch && !descMatch) {
                    continue;
                }
            }
            displayModules.add(m);
        }

        // Sort: enabled first, then alphabetically
        displayModules.sort(Comparator.comparing((Module m) -> !m.isEnabled())
                .thenComparing(Module::getName));
    }

    private int countEnabled() {
        if (BallsClientCore.getModuleManager() == null) return 0;
        int c = 0;
        for (Module m : BallsClientCore.getModuleManager().getModules()) {
            if (m.isEnabled()) c++;
        }
        return c;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Dark translucent gradient background
        graphics.fillGradient(0, 0, this.width, this.height, 0xD0080812, 0xF00D0D18);

        // Header panel background
        graphics.fill(0, 0, this.width, 56, 0xDD12121E);
        graphics.fill(0, 55, this.width, 56, 0xFF6366F1); // Accent separator line

        // Bottom panel background
        graphics.fill(0, this.height - 30, this.width, this.height, 0xDD12121E);
        graphics.fill(0, this.height - 31, this.width, this.height - 30, 0xFF312E81);

        // Title and client info
        int totalModules = BallsClientCore.getModuleManager() != null ? BallsClientCore.getModuleManager().getModules().size() : 0;
        graphics.drawString(this.font, "§d§lBallsClient§r §7v1.0.0", 16, 36, 0xFFFFFF);
        graphics.drawString(this.font, "§8" + totalModules + " Modules §7(" + countEnabled() + " ON)", this.width - 150, 36, 0xAAAAAA);

        // Render Category Tab Buttons
        for (CategoryButton cb : categoryButtons) {
            boolean isSelected = (cb.category == this.currentCategory);
            boolean isHovered = mouseX >= cb.x && mouseX <= cb.x + cb.width && mouseY >= cb.y && mouseY <= cb.y + cb.height;

            int bgColor = isSelected ? 0xFF6366F1 : (isHovered ? 0xFF2D2B52 : 0xFF1E1D36);
            int textColor = isSelected ? 0xFFFFFF : (isHovered ? 0xE0E0E0 : 0x9E9EB8);

            graphics.fill(cb.x, cb.y, cb.x + cb.width, cb.y + cb.height, bgColor);
            graphics.drawCenteredString(this.font, cb.label, cb.x + cb.width / 2, cb.y + 5, textColor);
        }

        // Render Module Cards list
        int listTop = 60;
        int listBottom = this.height - 34;
        int cardHeight = 34;
        int cardSpacing = 4;
        int cardWidth = Math.min(520, this.width - 40);
        int cardX = (this.width - cardWidth) / 2;

        graphics.enableScissor(0, listTop, this.width, listBottom);

        int currentY = listTop + 4 - scrollY;
        for (Module m : displayModules) {
            if (currentY + cardHeight >= listTop && currentY <= listBottom) {
                renderModuleCard(graphics, m, cardX, currentY, cardWidth, cardHeight, mouseX, mouseY);
            }
            currentY += cardHeight + cardSpacing;
        }

        graphics.disableScissor();

        // Render Settings overlay if a module is expanded
        if (settingsModule != null) {
            renderSettingsModal(graphics, settingsModule, mouseX, mouseY);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderModuleCard(GuiGraphics graphics, Module m, int x, int y, int width, int height, int mouseX, int mouseY) {
        boolean enabled = m.isEnabled();
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height && settingsModule == null;

        int bg = enabled ? (hovered ? 0xDD143220 : 0xCC11281A) : (hovered ? 0xDD222234 : 0xBB181826);
        int border = enabled ? 0xFF22C55E : 0xFF3B4252;

        graphics.fill(x, y, x + width, y + height, bg);
        graphics.fill(x, y, x + 3, y + height, border); // Left accent bar

        // Module Name
        String nameStr = (enabled ? "§a§l" : "§f") + m.getName();
        graphics.drawString(this.font, nameStr, x + 10, y + 5, 0xFFFFFF);

        // Category Tag
        String catStr = "§8[" + m.getCategory().name() + "]";
        graphics.drawString(this.font, catStr, x + 12 + this.font.width(nameStr), y + 6, 0x888888);

        // Description
        String desc = m.getDescription() != null ? m.getDescription() : "";
        if (desc.length() > 55) desc = desc.substring(0, 52) + "...";
        graphics.drawString(this.font, "§7" + desc, x + 10, y + 18, 0x999999);

        // Settings Button if module has settings
        Collection<Setting<?>> settings = m.getSettings();
        if (settings != null && !settings.isEmpty()) {
            int gearX = x + width - 145;
            int gearY = y + 7;
            boolean gearHover = mouseX >= gearX && mouseX <= gearX + 65 && mouseY >= gearY && mouseY <= gearY + 18;
            graphics.fill(gearX, gearY, gearX + 65, gearY + 18, gearHover ? 0xFF374151 : 0xFF1F2937);
            graphics.drawCenteredString(this.font, "§e⚙ " + settings.size(), gearX + 32, gearY + 5, 0xFFFFFF);
        }

        // Toggle Switch Pill Button
        int pillW = 60;
        int pillX = x + width - pillW - 8;
        int pillY = y + 7;
        int pillBg = enabled ? 0xFF16A34A : 0xFF374151;
        graphics.fill(pillX, pillY, pillX + pillW, pillY + 18, pillBg);
        graphics.drawCenteredString(this.font, enabled ? "ON" : "OFF", pillX + pillW / 2, pillY + 5, 0xFFFFFF);
    }

    private void renderSettingsModal(GuiGraphics graphics, Module m, int mouseX, int mouseY) {
        // Dim background
        graphics.fill(0, 0, this.width, this.height, 0xB0000000);

        int modalW = Math.min(420, this.width - 40);
        int modalH = Math.min(260, this.height - 60);
        int modalX = (this.width - modalW) / 2;
        int modalY = (this.height - modalH) / 2;

        graphics.fill(modalX, modalY, modalX + modalW, modalY + modalH, 0xFF181828);
        graphics.fill(modalX, modalY, modalX + modalW, modalY + 28, 0xFF242438);
        graphics.fill(modalX, modalY + 27, modalX + modalW, modalY + 28, 0xFF6366F1);

        // Modal Title
        graphics.drawString(this.font, "§d⚙ " + m.getName() + " Settings", modalX + 12, modalY + 10, 0xFFFFFF);

        // Close X button
        int closeX = modalX + modalW - 24;
        int closeY = modalY + 8;
        boolean closeHover = mouseX >= closeX && mouseX <= closeX + 16 && mouseY >= closeY && mouseY <= closeY + 16;
        graphics.drawString(this.font, closeHover ? "§c[X]" : "§7[X]", closeX, closeY, 0xFFFFFF);

        // Render Settings Rows
        Collection<Setting<?>> settings = m.getSettings();
        if (settings == null || settings.isEmpty()) {
            graphics.drawCenteredString(this.font, "§7No configurable settings for this module.", modalX + modalW / 2, modalY + 80, 0x888888);
            return;
        }

        int sY = modalY + 36;
        int rowH = 26;
        for (Setting<?> s : settings) {
            if (sY + rowH > modalY + modalH - 10) break;

            graphics.fill(modalX + 10, sY, modalX + modalW - 10, sY + rowH - 2, 0xFF202032);
            graphics.drawString(this.font, s.getName(), modalX + 16, sY + 7, 0xEEEEEE);

            // Render interactive value box on the right
            int valW = 90;
            int valX = modalX + modalW - valW - 16;
            int valY = sY + 3;

            if (s instanceof BooleanSetting bs) {
                boolean bVal = bs.getValue();
                graphics.fill(valX, valY, valX + valW, valY + 16, bVal ? 0xFF16A34A : 0xFF4B5563);
                graphics.drawCenteredString(this.font, bVal ? "ENABLED" : "DISABLED", valX + valW / 2, valY + 4, 0xFFFFFF);
            } else if (s instanceof NumberSetting ns) {
                graphics.fill(valX, valY, valX + valW, valY + 16, 0xFF374151);
                graphics.drawCenteredString(this.font, "« " + ns.getValue() + " »", valX + valW / 2, valY + 4, 0x60A5FA);
            } else if (s instanceof ModeSetting ms) {
                graphics.fill(valX, valY, valX + valW, valY + 16, 0xFF374151);
                graphics.drawCenteredString(this.font, "§e" + ms.getValue(), valX + valW / 2, valY + 4, 0xFFFFFF);
            } else {
                graphics.fill(valX, valY, valX + valW, valY + 16, 0xFF2D3748);
                graphics.drawCenteredString(this.font, String.valueOf(s.getValue()), valX + valW / 2, valY + 4, 0xCCCCCC);
            }

            sY += rowH;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // If settings modal is open:
        if (settingsModule != null) {
            int modalW = Math.min(420, this.width - 40);
            int modalH = Math.min(260, this.height - 60);
            int modalX = (this.width - modalW) / 2;
            int modalY = (this.height - modalH) / 2;

            // Close button clicked
            if (mouseX >= modalX + modalW - 28 && mouseX <= modalX + modalW - 8 && mouseY >= modalY + 6 && mouseY <= modalY + 22) {
                settingsModule = null;
                return true;
            }

            // Clicked outside modal closes it
            if (mouseX < modalX || mouseX > modalX + modalW || mouseY < modalY || mouseY > modalY + modalH) {
                settingsModule = null;
                return true;
            }

            // Clicked a setting row
            Collection<Setting<?>> settings = settingsModule.getSettings();
            if (settings != null) {
                int sY = modalY + 36;
                int rowH = 26;
                for (Setting<?> s : settings) {
                    int valW = 90;
                    int valX = modalX + modalW - valW - 16;
                    int valY = sY + 3;

                    if (mouseX >= valX && mouseX <= valX + valW && mouseY >= valY && mouseY <= valY + 16) {
                        if (s instanceof BooleanSetting bs) {
                            bs.setValue(!bs.getValue());
                        } else if (s instanceof NumberSetting ns) {
                            // Left click increments, right click decrements
                            double cur = ((Number) ns.getValue()).doubleValue();
                            double step = ns.getStep() > 0 ? ns.getStep() : 1.0;
                            double next = (button == 0) ? (cur + step) : (cur - step);
                            ns.setValue(next);
                        } else if (s instanceof ModeSetting ms) {
                            ms.cycle();
                        }
                        if (BallsClientCore.getModuleManager() != null) {
                            BallsClientCore.getModuleManager().saveConfig();
                        }
                        return true;
                    }
                    sY += rowH;
                }
            }
            return true;
        }

        // Check Category Tabs
        for (CategoryButton cb : categoryButtons) {
            if (mouseX >= cb.x && mouseX <= cb.x + cb.width && mouseY >= cb.y && mouseY <= cb.y + cb.height) {
                this.currentCategory = cb.category;
                this.scrollY = 0;
                updateFilteredModules();
                return true;
            }
        }

        // Check Module Cards
        int listTop = 60;
        int listBottom = this.height - 34;
        if (mouseY >= listTop && mouseY <= listBottom) {
            int cardHeight = 34;
            int cardSpacing = 4;
            int cardWidth = Math.min(520, this.width - 40);
            int cardX = (this.width - cardWidth) / 2;

            int currentY = listTop + 4 - scrollY;
            for (Module m : displayModules) {
                if (mouseY >= currentY && mouseY <= currentY + cardHeight && mouseX >= cardX && mouseX <= cardX + cardWidth) {
                    int gearX = cardX + cardWidth - 145;
                    int gearY = currentY + 7;

                    // If gear clicked or right-clicked: open settings
                    if (button == 1 || (mouseX >= gearX && mouseX <= gearX + 65 && mouseY >= gearY && mouseY <= gearY + 18)) {
                        this.settingsModule = m;
                        return true;
                    }

                    // Otherwise toggle module
                    m.toggle();
                    if (BallsClientCore.getModuleManager() != null) {
                        BallsClientCore.getModuleManager().saveConfig();
                    }
                    return true;
                }
                currentY += cardHeight + cardSpacing;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (settingsModule == null) {
            int maxScroll = Math.max(0, displayModules.size() * 38 - (this.height - 100));
            this.scrollY = Math.max(0, Math.min(maxScroll, this.scrollY - (int)(verticalAmount * 24)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            if (settingsModule != null) {
                settingsModule = null;
                return true;
            }
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        if (BallsClientCore.getModuleManager() != null) {
            BallsClientCore.getModuleManager().saveConfig();
        }
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
