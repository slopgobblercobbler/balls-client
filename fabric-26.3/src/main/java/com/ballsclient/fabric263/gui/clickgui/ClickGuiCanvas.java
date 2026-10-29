package com.ballsclient.fabric263.gui.clickgui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Universal drawing canvas bridging between 1.21 GuiGraphics and 26.3 GuiGraphicsExtractor.
 */
public interface ClickGuiCanvas {
    void fill(int x1, int y1, int x2, int y2, int color);
    void fillGradient(int x1, int y1, int x2, int y2, int color1, int color2);
    void renderOutline(int x, int y, int width, int height, int color);
    default void drawBorder(int x, int y, int width, int height, int color) {
        renderOutline(x, y, width, height, color);
    }
    void drawString(Font font, String text, int x, int y, int color, boolean shadow);
    default void drawString(Font font, String text, int x, int y, int color) {
        drawString(font, text, x, y, color, false);
    }
    void enableScissor(int x1, int y1, int x2, int y2);
    void disableScissor();
    void renderComponentTooltip(Font font, List<Component> lines, int mouseX, int mouseY);

    static ClickGuiCanvas of(Object graphicsOrExtractor) {
        if (graphicsOrExtractor instanceof GuiGraphics g) {
            return new GuiGraphicsCanvas(g);
        }
        if (graphicsOrExtractor instanceof GuiGraphicsExtractor e) {
            return new ExtractorCanvas(e);
        }
        throw new IllegalArgumentException("Unknown canvas context: " + graphicsOrExtractor);
    }

    class GuiGraphicsCanvas implements ClickGuiCanvas {
        private final GuiGraphics g;

        public GuiGraphicsCanvas(GuiGraphics g) {
            this.g = g;
        }

        @Override
        public void fill(int x1, int y1, int x2, int y2, int color) {
            g.fill(x1, y1, x2, y2, color);
        }

        @Override
        public void fillGradient(int x1, int y1, int x2, int y2, int color1, int color2) {
            g.fillGradient(x1, y1, x2, y2, color1, color2);
        }

        @Override
        public void renderOutline(int x, int y, int width, int height, int color) {
            g.renderOutline(x, y, width, height, color);
        }

        @Override
        public void drawString(Font font, String text, int x, int y, int color, boolean shadow) {
            g.drawString(font, text, x, y, color, shadow);
        }

        @Override
        public void enableScissor(int x1, int y1, int x2, int y2) {
            g.enableScissor(x1, y1, x2, y2);
        }

        @Override
        public void disableScissor() {
            g.disableScissor();
        }

        @Override
        public void renderComponentTooltip(Font font, List<Component> lines, int mouseX, int mouseY) {
            g.renderComponentTooltip(font, lines, mouseX, mouseY);
        }
    }

    class ExtractorCanvas implements ClickGuiCanvas {
        private final GuiGraphicsExtractor extractor;

        public ExtractorCanvas(GuiGraphicsExtractor extractor) {
            this.extractor = extractor;
        }

        @Override
        public void fill(int x1, int y1, int x2, int y2, int color) {
            extractor.fill(x1, y1, x2, y2, color);
        }

        @Override
        public void fillGradient(int x1, int y1, int x2, int y2, int color1, int color2) {
            extractor.fillGradient(x1, y1, x2, y2, color1, color2);
        }

        @Override
        public void renderOutline(int x, int y, int width, int height, int color) {
            extractor.outline(x, y, width, height, color);
        }

        @Override
        public void drawString(Font font, String text, int x, int y, int color, boolean shadow) {
            extractor.text(font, text, x, y, color, shadow);
        }

        @Override
        public void enableScissor(int x1, int y1, int x2, int y2) {
            extractor.enableScissor(x1, y1, x2, y2);
        }

        @Override
        public void disableScissor() {
            extractor.disableScissor();
        }

        @Override
        public void renderComponentTooltip(Font font, List<Component> lines, int mouseX, int mouseY) {
            extractor.setComponentTooltipForNextFrame(font, lines, mouseX, mouseY);
        }
    }
}
