package com.ballsclient.fabric263.gui;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import com.ballsclient.core.setting.BooleanSetting;
import com.ballsclient.core.setting.ModeSetting;
import com.ballsclient.core.setting.NumberSetting;
import com.ballsclient.core.setting.Setting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.ballsclient.fabric263.gui.clickgui.ModernClickGuiScreen;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class BallsClientConfigScreen extends Screen {
    private static final int MODULES_PER_PAGE = 8;

    private final Screen parent;
    private Category currentCategory = null; // null = ALL
    private String searchText = "";
    private int currentPage = 0;
    private Module settingsModule = null; // When non-null, viewing settings for this module

    private final List<Module> filteredModules = new ArrayList<>();
    private EditBox searchBox;

    public BallsClientConfigScreen(Screen parent) {
        super(Component.literal("BallsClient Dashboard"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        rebuildUI();
    }

    private void rebuildUI() {
        this.clearWidgets();

        if (settingsModule != null) {
            buildSettingsUI();
        } else {
            buildMainUI();
        }
    }

    private void updateFilteredModules() {
        filteredModules.clear();
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
            filteredModules.add(m);
        }

        filteredModules.sort(Comparator.comparing((Module m) -> !m.isEnabled())
                .thenComparing(Module::getName));
    }

    private void buildMainUI() {
        updateFilteredModules();

        int totalModules = BallsClientCore.getModuleManager() != null ? BallsClientCore.getModuleManager().getModules().size() : 0;
        int enabledModules = 0;
        if (BallsClientCore.getModuleManager() != null) {
            for (Module m : BallsClientCore.getModuleManager().getModules()) {
                if (m.isEnabled()) enabledModules++;
            }
        }

        // 1. Header Banner Button (non-clickable)
        this.addRenderableWidget(Button.builder(
                Component.literal("§d§lBallsClient§r §7v1.0.0 — §e" + totalModules + " Modules §7(" + enabledModules + " ON) §b[Modern GUI]"),
                b -> ScreenHelper.setScreen(this.minecraft, new ModernClickGuiScreen(this.parent))
        ).bounds(this.width / 2 - 170, 6, 340, 18).build());

        // 2. Category Tabs Row (Y = 28)
        Category[] cats = Category.values();
        int totalTabs = cats.length + 1; // +1 for ALL
        int tabPad = 2;
        int maxTabRowWidth = Math.min(this.width - 20, 480);
        int tabW = Math.max(38, (maxTabRowWidth - (totalTabs * tabPad)) / totalTabs);
        int startX = (this.width - (totalTabs * (tabW + tabPad))) / 2;

        // "ALL" tab
        String allLabel = (currentCategory == null) ? "▶ ALL ◀" : "ALL";
        this.addRenderableWidget(Button.builder(Component.literal(allLabel), b -> {
            this.currentCategory = null;
            this.currentPage = 0;
            rebuildUI();
        }).bounds(startX, 28, tabW, 18).build());

        for (int i = 0; i < cats.length; i++) {
            Category cat = cats[i];
            String label = (currentCategory == cat) ? ("▶ " + cat.name() + " ◀") : cat.name();
            int x = startX + (i + 1) * (tabW + tabPad);
            this.addRenderableWidget(Button.builder(Component.literal(label), b -> {
                this.currentCategory = cat;
                this.currentPage = 0;
                rebuildUI();
            }).bounds(x, 28, tabW, 18).build());
        }

        // 3. Search Bar (Y = 49)
        int searchW = Math.min(280, this.width - 40);
        int searchX = this.width / 2 - searchW / 2;
        searchBox = new EditBox(this.font, searchX, 49, searchW, 16, Component.literal("Search..."));
        searchBox.setValue(this.searchText);
        searchBox.setResponder(text -> {
            this.searchText = text != null ? text.trim().toLowerCase() : "";
            this.currentPage = 0;
            rebuildUI();
        });
        this.addRenderableWidget(searchBox);

        // 4. Module Rows (Y = 70 onwards)
        int totalFiltered = filteredModules.size();
        int maxPages = Math.max(1, (int) Math.ceil((double) totalFiltered / MODULES_PER_PAGE));
        if (currentPage >= maxPages) currentPage = maxPages - 1;
        if (currentPage < 0) currentPage = 0;

        int startIndex = currentPage * MODULES_PER_PAGE;
        int endIndex = Math.min(startIndex + MODULES_PER_PAGE, totalFiltered);

        int rowStartY = 68;
        int rowHeight = 20;
        int rowPad = 2;
        int cardWidth = Math.min(360, this.width - 30);
        int cardX = this.width / 2 - cardWidth / 2;

        for (int i = startIndex; i < endIndex; i++) {
            final Module m = filteredModules.get(i);
            int y = rowStartY + (i - startIndex) * (rowHeight + rowPad);

            boolean hasSettings = (m.getSettings() != null && !m.getSettings().isEmpty());
            int toggleWidth = hasSettings ? (cardWidth - 36) : cardWidth;

            // Module Toggle Button
            String status = m.isEnabled() ? "§a[ON] " : "§c[OFF] ";
            String modLabel = status + m.getName() + " §8[" + m.getCategory().name() + "]";
            this.addRenderableWidget(Button.builder(Component.literal(modLabel), b -> {
                m.toggle();
                if (BallsClientCore.getModuleManager() != null) {
                    BallsClientCore.getModuleManager().saveConfig();
                }
                rebuildUI();
            }).bounds(cardX, y, toggleWidth, rowHeight).build());

            // Settings Button if module has settings
            if (hasSettings) {
                this.addRenderableWidget(Button.builder(Component.literal("⚙"), b -> {
                    this.settingsModule = m;
                    rebuildUI();
                }).bounds(cardX + toggleWidth + 2, y, 34, rowHeight).build());
            }
        }

        // 5. Pagination Bar (Y = height - 48)
        int navY = Math.max(rowStartY + MODULES_PER_PAGE * (rowHeight + rowPad) + 4, this.height - 48);
        int pageBtnW = 60;
        int labelW = 140;
        int navTotalW = pageBtnW * 2 + labelW + 8;
        int navStartX = this.width / 2 - navTotalW / 2;

        // Prev Button
        Button prevBtn = Button.builder(Component.literal("« Prev"), b -> {
            if (currentPage > 0) {
                currentPage--;
                rebuildUI();
            }
        }).bounds(navStartX, navY, pageBtnW, 18).build();
        prevBtn.active = (currentPage > 0);
        this.addRenderableWidget(prevBtn);

        // Page Indicator
        String pageInfo = "Page " + (currentPage + 1) + " / " + maxPages + " (" + totalFiltered + ")";
        this.addRenderableWidget(Button.builder(Component.literal(pageInfo), b -> {}).bounds(navStartX + pageBtnW + 4, navY, labelW, 18).build());

        // Next Button
        Button nextBtn = Button.builder(Component.literal("Next »"), b -> {
            if (currentPage < maxPages - 1) {
                currentPage++;
                rebuildUI();
            }
        }).bounds(navStartX + pageBtnW + labelW + 8, navY, pageBtnW, 18).build();
        nextBtn.active = (currentPage < maxPages - 1);
        this.addRenderableWidget(nextBtn);

        // 6. Save & Close Button (Y = height - 26)
        int closeW = 130;
        this.addRenderableWidget(Button.builder(Component.literal("Save & Close"), b -> this.onClose())
                .bounds(this.width / 2 - closeW / 2, this.height - 26, closeW, 20)
                .build());
    }

    private void buildSettingsUI() {
        if (settingsModule == null) {
            rebuildUI();
            return;
        }

        int modalW = Math.min(380, this.width - 20);
        int modalX = this.width / 2 - modalW / 2;

        // 1. Module Title Banner
        this.addRenderableWidget(Button.builder(
                Component.literal("§d⚙ " + settingsModule.getName() + " Settings §8[" + settingsModule.getCategory().name() + "]"),
                b -> {}
        ).bounds(modalX, 10, modalW, 20).build());

        // 2. Settings Items
        Collection<Setting<?>> settings = settingsModule.getSettings();
        int sY = 36;
        int itemH = 20;
        int itemPad = 3;

        if (settings == null || settings.isEmpty()) {
            this.addRenderableWidget(Button.builder(Component.literal("§7No configurable options for this module."), b -> {})
                    .bounds(modalX, 60, modalW, 20).build());
        } else {
            for (Setting<?> s : settings) {
                if (s instanceof BooleanSetting bs) {
                    boolean val = bs.getValue();
                    String btnText = s.getName() + ": " + (val ? "§a[ENABLED]" : "§c[DISABLED]");
                    this.addRenderableWidget(Button.builder(Component.literal(btnText), b -> {
                        bs.setValue(!bs.getValue());
                        if (BallsClientCore.getModuleManager() != null) {
                            BallsClientCore.getModuleManager().saveConfig();
                        }
                        rebuildUI();
                    }).bounds(modalX, sY, modalW, itemH).build());
                } else if (s instanceof NumberSetting ns) {
                    int stepBtnW = 28;
                    int labelW = modalW - (stepBtnW * 2) - 4;

                    // Dec Button
                    this.addRenderableWidget(Button.builder(Component.literal("« -"), b -> {
                        double cur = ((Number) ns.getValue()).doubleValue();
                        double step = ns.getStep() > 0 ? ns.getStep() : 1.0;
                        ns.setValue(cur - step);
                        if (BallsClientCore.getModuleManager() != null) {
                            BallsClientCore.getModuleManager().saveConfig();
                        }
                        rebuildUI();
                    }).bounds(modalX, sY, stepBtnW, itemH).build());

                    // Display Button
                    String numText = s.getName() + ": §b" + ns.getValue();
                    this.addRenderableWidget(Button.builder(Component.literal(numText), b -> {}).bounds(modalX + stepBtnW + 2, sY, labelW, itemH).build());

                    // Inc Button
                    this.addRenderableWidget(Button.builder(Component.literal("+ »"), b -> {
                        double cur = ((Number) ns.getValue()).doubleValue();
                        double step = ns.getStep() > 0 ? ns.getStep() : 1.0;
                        ns.setValue(cur + step);
                        if (BallsClientCore.getModuleManager() != null) {
                            BallsClientCore.getModuleManager().saveConfig();
                        }
                        rebuildUI();
                    }).bounds(modalX + stepBtnW + labelW + 4, sY, stepBtnW, itemH).build());
                } else if (s instanceof ModeSetting ms) {
                    String modeText = s.getName() + ": §e" + ms.getValue();
                    this.addRenderableWidget(Button.builder(Component.literal(modeText), b -> {
                        ms.cycle();
                        if (BallsClientCore.getModuleManager() != null) {
                            BallsClientCore.getModuleManager().saveConfig();
                        }
                        rebuildUI();
                    }).bounds(modalX, sY, modalW, itemH).build());
                } else {
                    String genText = s.getName() + ": " + s.getValue();
                    this.addRenderableWidget(Button.builder(Component.literal(genText), b -> {}).bounds(modalX, sY, modalW, itemH).build());
                }

                sY += itemH + itemPad;
                if (sY > this.height - 56) break;
            }
        }

        // 3. Back Button
        int backW = 140;
        this.addRenderableWidget(Button.builder(Component.literal("« Back to Module List"), b -> {
            this.settingsModule = null;
            rebuildUI();
        }).bounds(this.width / 2 - backW / 2, this.height - 28, backW, 20).build());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            if (settingsModule != null) {
                settingsModule = null;
                rebuildUI();
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
        ScreenHelper.setScreen(this.minecraft, this.parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
