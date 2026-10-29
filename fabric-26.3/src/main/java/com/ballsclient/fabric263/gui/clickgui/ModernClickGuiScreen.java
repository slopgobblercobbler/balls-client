package com.ballsclient.fabric263.gui.clickgui;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.module.Category;
import com.ballsclient.fabric263.gui.ScreenHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Modern Draggable ClickGUI Screen for Balls Client.
 * Features translucent dark theme (#121216), 6 draggable category panels,
 * scissor-clipped scrollable viewports, expandable settings, and inline keybind listeners.
 */
public class ModernClickGuiScreen extends Screen {
    private final Screen parent;
    private final List<CategoryPanel> panels = new ArrayList<>();

    private String hoveredModuleName = null;
    private String hoveredModuleDesc = null;

    public ModernClickGuiScreen(Screen parent) {
        super(Component.literal("Balls Client Modern ClickGUI"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        if (panels.isEmpty()) {
            buildPanels();
        } else {
            // Screen resized: update modules and clamp bounds
            for (CategoryPanel p : panels) {
                p.loadModules();
                p.clampToBounds(this.width, this.height);
            }
        }
    }

    private void buildPanels() {
        panels.clear();
        ClickGuiConfig.load();

        int startX = 20;
        int startY = 30;
        int panelW = 125;
        int gap = 12;

        panels.add(new CategoryPanel("Combat", 0xFFED4245, startX, startY, Category.COMBAT));
        panels.add(new CategoryPanel("Movement", 0xFF00E5FF, startX + (panelW + gap), startY, Category.MOVEMENT));
        panels.add(new CategoryPanel("Render", 0xFF9B59B6, startX + (panelW + gap) * 2, startY, Category.RENDER));
        panels.add(new CategoryPanel("Player", 0xFF43B581, startX + (panelW + gap) * 3, startY, Category.PLAYER));
        panels.add(new CategoryPanel("World", 0xFFFFAA00, startX + (panelW + gap) * 4, startY, Category.INVENTORY, Category.AUTOMATION));
        panels.add(new CategoryPanel("Misc", 0xFF5865F2, startX + (panelW + gap) * 5, startY, Category.NETWORK));

        // Restore persisted states or wrap if screen is narrow
        int curX = startX;
        int curY = startY;

        for (CategoryPanel panel : panels) {
            ClickGuiConfig.PanelState state = ClickGuiConfig.getPanelState(panel.getTitle());
            if (state != null) {
                panel.setX(state.x);
                panel.setY(state.y);
                panel.setCollapsed(state.collapsed);
            } else {
                if (curX + panelW > this.width - 10 && curX > startX) {
                    curX = startX;
                    curY += 130;
                }
                panel.setX(curX);
                panel.setY(curY);
                curX += panelW + gap;
            }
            panel.clampToBounds(this.width, this.height);
        }
    }

    private void persistPanelPositions() {
        for (CategoryPanel panel : panels) {
            ClickGuiConfig.setPanelState(panel.getTitle(), panel.getX(), panel.getY(), panel.isCollapsed());
        }
        ClickGuiConfig.save();
    }

    private ModuleCard findRebindingCard() {
        for (CategoryPanel panel : panels) {
            for (ModuleCard card : panel.getCards()) {
                if (card.isRebinding()) {
                    return card;
                }
            }
        }
        return null;
    }

    /**
     * Primary universal render routine for ClickGUI.
     */
    public void render(ClickGuiCanvas canvas, int mouseX, int mouseY, float delta) {
        try {
            // Translucent dark theme backdrop (#121216 @ ~70% opacity)
            canvas.fill(0, 0, this.width, this.height, 0xB3121216);

            // Header watermark
            canvas.drawString(this.font, "BALLS CLIENT", 12, 10, 0xFF5865F2, true);
            canvas.drawString(this.font, "§7v1.0.0 | Press §bESC§7 or §bRight Shift§7 to close", 92, 10, 0xFF8A8E9E, false);

            hoveredModuleName = null;
            hoveredModuleDesc = null;

            // Render category panels (back-to-front)
            for (CategoryPanel panel : panels) {
                panel.render(canvas, this.font, mouseX, mouseY, this.width, this.height, (name, desc) -> {
                    hoveredModuleName = name;
                    hoveredModuleDesc = desc;
                });
            }

            // Render deferred tooltip at top level (prevents clipping by scissor boxes)
            if (hoveredModuleName != null && findRebindingCard() == null) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.literal("§f§l" + hoveredModuleName));
                if (hoveredModuleDesc != null && !hoveredModuleDesc.isEmpty()) {
                    tooltip.add(Component.literal("§7" + hoveredModuleDesc));
                }
                tooltip.add(Component.literal("§8[L-Click] Toggle  |  [R-Click] Settings"));
                canvas.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
            }
        } catch (Throwable t) {
            BallsClientCore.LOGGER.error("Error during ModernClickGuiScreen render: " + t.getMessage(), t);
        }
    }

    /**
     * Minecraft 26.3 Background suppression entry point.
     * Prevents vanilla Screen.extractBackground from rendering dirt texture or dark blur.
     */
    public void extractBackground(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        // No-op: ModernClickGuiScreen renders its own translucent dark background in extractRenderState
    }

    /**
     * Minecraft 26.3 Screen rendering entry point.
     * Invoked natively by Minecraft 26.3 Screen.extractRenderStateWithTooltipAndSubtitles.
     */
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        render(ClickGuiCanvas.of(extractor), mouseX, mouseY, delta);
    }

    /**
     * Minecraft 1.20/1.21 Screen rendering entry point.
     */
    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        render(ClickGuiCanvas.of(context), mouseX, mouseY, delta);
    }

    /**
     * Minecraft 26.3 Mouse Click entry point.
     */
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event == null) return false;
        return mouseClicked(event.x(), event.y(), event.button());
    }

    /**
     * Minecraft 26.3 Mouse Release entry point.
     */
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event == null) return false;
        return mouseReleased(event.x(), event.y(), event.button());
    }

    /**
     * Minecraft 26.3 Mouse Drag entry point.
     */
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        if (event == null) return false;
        return mouseDragged(event.x(), event.y(), event.button(), deltaX, deltaY);
    }

    /**
     * Minecraft 26.3 Key Press entry point.
     */
    public boolean keyPressed(KeyEvent event) {
        if (event == null) return false;
        return keyPressed(event.key(), event.keycode(), event.modifiers());
    }

    // --- Legacy coordinate-based mouse & keyboard handlers ---

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ModuleCard rebindingCard = findRebindingCard();

        for (int i = panels.size() - 1; i >= 0; i--) {
            CategoryPanel panel = panels.get(i);
            if (panel.mouseClicked(mouseX, mouseY, button, this.height)) {
                if (panel.isDragging()) {
                    panels.remove(i);
                    panels.add(panel);
                }
                return true;
            }
        }

        if (rebindingCard != null) {
            rebindingCard.setRebinding(false);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean handled = false;
        for (CategoryPanel panel : panels) {
            if (panel.mouseReleased(mouseX, mouseY, button)) {
                handled = true;
            }
        }
        persistPanelPositions();
        return handled;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (int i = panels.size() - 1; i >= 0; i--) {
            CategoryPanel panel = panels.get(i);
            if (panel.mouseDragged(mouseX, mouseY, button, deltaX, deltaY, this.width, this.height)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (int i = panels.size() - 1; i >= 0; i--) {
            CategoryPanel panel = panels.get(i);
            if (panel.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount, this.height)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        ModuleCard rebindingCard = findRebindingCard();
        if (rebindingCard != null) {
            return rebindingCard.handleKey(keyCode);
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.onClose();
            return true;
        }

        return false;
    }

    public boolean keyPressed(com.ballsclient.core.event.KeyEvent keyEvent) {
        if (keyEvent != null) {
            return keyPressed(keyEvent.getKeyCode(), keyEvent.getScanCode(), keyEvent.getModifiers());
        }
        return false;
    }

    @Override
    public void onClose() {
        persistPanelPositions();
        if (BallsClientCore.getModuleManager() != null) {
            BallsClientCore.getModuleManager().saveConfig();
        }
        ScreenHelper.setScreen(this.minecraft, this.parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public boolean isInGameUi() {
        return true;
    }

    public List<CategoryPanel> getPanels() {
        return panels;
    }
}
