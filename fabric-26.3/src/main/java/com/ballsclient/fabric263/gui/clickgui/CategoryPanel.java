package com.ballsclient.fabric263.gui.clickgui;

import com.ballsclient.core.BallsClientCore;
import com.ballsclient.core.module.Category;
import com.ballsclient.core.module.Module;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Draggable category panel containing module cards, supporting vertical scrolling and OpenGL scissor clipping.
 */
public class CategoryPanel {
    private final String title;
    private final List<Category> mappedCategories;
    private final int accentColor;
    private final List<ModuleCard> cards = new ArrayList<>();

    private int x;
    private int y;
    private int width = 125;
    private int headerHeight = 20;

    private boolean collapsed = false;
    private boolean dragging = false;
    private int dragOffsetX;
    private int dragOffsetY;
    private int scrollOffset = 0;

    public CategoryPanel(String title, int accentColor, int defaultX, int defaultY, Category... mappedCategories) {
        this.title = title;
        this.accentColor = accentColor;
        this.x = defaultX;
        this.y = defaultY;
        this.mappedCategories = Arrays.asList(mappedCategories);
        loadModules();
    }

    public void loadModules() {
        cards.clear();
        if (BallsClientCore.getModuleManager() == null) {
            return;
        }

        List<Module> matched = new ArrayList<>();
        for (Module m : BallsClientCore.getModuleManager().getModules()) {
            if (m != null && mappedCategories.contains(m.getCategory())) {
                matched.add(m);
            }
        }

        matched.sort(Comparator.comparing(Module::getName));
        for (Module m : matched) {
            cards.add(new ModuleCard(m));
        }
    }

    public String getTitle() {
        return title;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public boolean isDragging() {
        return dragging;
    }

    public List<ModuleCard> getCards() {
        return cards;
    }

    public int getTotalCardsHeight() {
        int h = 0;
        for (ModuleCard card : cards) {
            h += card.getHeight();
        }
        return h;
    }

    public int getViewportHeight(int screenHeight) {
        if (collapsed) {
            return 0;
        }
        int totalH = getTotalCardsHeight();
        int maxH = Math.max(60, screenHeight - y - headerHeight - 15);
        return Math.min(totalH, maxH);
    }

    public void clampToBounds(int screenWidth, int screenHeight) {
        this.x = Math.max(2, Math.min(this.x, screenWidth - this.width - 2));
        this.y = Math.max(2, Math.min(this.y, screenHeight - this.headerHeight - 2));
    }

    public void render(GuiGraphics context, Font font, int mouseX, int mouseY, int screenWidth, int screenHeight, BiConsumer<String, String> tooltipConsumer) {
        render(ClickGuiCanvas.of(context), font, mouseX, mouseY, screenWidth, screenHeight, tooltipConsumer);
    }

    public void render(ClickGuiCanvas context, Font font, int mouseX, int mouseY, int screenWidth, int screenHeight, BiConsumer<String, String> tooltipConsumer) {
        // Render Header bar
        // Top accent line
        context.fill(x, y, x + width, y + 2, accentColor);
        // Header body
        context.fillGradient(x, y + 2, x + width, y + headerHeight, 0xFF1E1F29, 0xFF14151D);
        context.renderOutline(x, y, width, headerHeight, 0xFF2C2E3C);

        // Header Title
        context.drawString(font, title, x + 6, y + 6, 0xFFFFFFFF, false);

        // Module count badge
        String badge = "[" + cards.size() + "]";
        int badgeX = x + font.width(title) + 10;
        context.drawString(font, badge, badgeX, y + 6, 0xFF72767D, false);

        // Collapse/expand toggle icon
        String toggleIcon = collapsed ? "+" : "-";
        int iconW = font.width(toggleIcon);
        context.drawString(font, toggleIcon, x + width - iconW - 6, y + 6, 0xFFB0B2C0, false);

        if (collapsed) {
            return;
        }

        // Render Module list with Scissor clipping
        int totalContentH = getTotalCardsHeight();
        int viewportH = getViewportHeight(screenHeight);
        int maxScroll = Math.max(0, totalContentH - viewportH);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        int viewportY = y + headerHeight;

        // Viewport background
        context.fill(x, viewportY, x + width, viewportY + viewportH, 0xF0121319);

        // Scissor clipping
        context.enableScissor(x, viewportY, x + width, viewportY + viewportH);

        int curY = viewportY - scrollOffset;
        for (ModuleCard card : cards) {
            int cardH = card.getHeight();
            // Render only if card intersects viewport
            if (curY + cardH >= viewportY && curY <= viewportY + viewportH) {
                card.render(context, font, x, curY, width, mouseX, mouseY, tooltipConsumer);
            }
            curY += cardH;
        }

        context.disableScissor();

        // Outline around viewport
        context.renderOutline(x, viewportY, width, viewportH, 0xFF242634);

        // Sleek scrollbar
        if (totalContentH > viewportH && viewportH > 0) {
            int trackX = x + width - 3;
            int trackH = viewportH;
            context.fill(trackX, viewportY, trackX + 3, viewportY + trackH, 0x44000000);

            double scrollRatio = maxScroll > 0 ? (double) scrollOffset / maxScroll : 0.0;
            int thumbH = Math.max(12, (int) (viewportH * ((double) viewportH / totalContentH)));
            int thumbY = viewportY + (int) ((viewportH - thumbH) * scrollRatio);
            context.fill(trackX, thumbY, trackX + 3, thumbY + thumbH, accentColor);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, int screenHeight) {
        // Header click
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + headerHeight) {
            if (button == 0) {
                // Dragging start
                this.dragging = true;
                this.dragOffsetX = (int) mouseX - x;
                this.dragOffsetY = (int) mouseY - y;
                return true;
            } else if (button == 1) {
                // Collapse toggle
                this.collapsed = !this.collapsed;
                return true;
            }
        }

        if (collapsed) {
            return false;
        }

        // Viewport card click
        int viewportH = getViewportHeight(screenHeight);
        int viewportY = y + headerHeight;

        if (mouseX >= x && mouseX <= x + width && mouseY >= viewportY && mouseY < viewportY + viewportH) {
            int curY = viewportY - scrollOffset;
            for (ModuleCard card : cards) {
                int cardH = card.getHeight();
                if (mouseY >= curY && mouseY < curY + cardH) {
                    return card.mouseClicked(mouseX, mouseY, button);
                }
                curY += cardH;
            }
        }

        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean wasDragging = this.dragging;
        this.dragging = false;

        boolean consumed = wasDragging;
        if (!collapsed) {
            for (ModuleCard card : cards) {
                if (card.mouseReleased(mouseX, mouseY, button)) {
                    consumed = true;
                }
            }
        }
        return consumed;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, int screenWidth, int screenHeight) {
        if (dragging && button == 0) {
            this.x = (int) mouseX - dragOffsetX;
            this.y = (int) mouseY - dragOffsetY;
            clampToBounds(screenWidth, screenHeight);
            return true;
        }

        if (!collapsed) {
            for (ModuleCard card : cards) {
                if (card.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount, int screenHeight) {
        if (collapsed) {
            return false;
        }

        int viewportH = getViewportHeight(screenHeight);
        int viewportY = y + headerHeight;

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= viewportY + viewportH) {
            int totalContentH = getTotalCardsHeight();
            int maxScroll = Math.max(0, totalContentH - viewportH);
            if (maxScroll > 0) {
                this.scrollOffset -= (int) (verticalAmount * 18);
                this.scrollOffset = Math.max(0, Math.min(this.scrollOffset, maxScroll));
                return true;
            }
        }
        return false;
    }
}
