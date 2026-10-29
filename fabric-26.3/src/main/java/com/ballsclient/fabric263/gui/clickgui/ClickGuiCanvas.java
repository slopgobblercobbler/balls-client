package com.ballsclient.fabric263.gui.clickgui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Universal drawing canvas bridging between 1.21 GuiGraphics and 26.3 GuiGraphicsExtractor.
 */
public interface ClickGuiCanvas {
    void fill(int x1, int y1, int x2, int y2, int color);
    void fillGradient(int x1, int y1, int x2, int y2, int color1, int color2);
    void renderOutline(int x, int y, int width, int height, int color);
    void drawString(Font font, String text, int x, int y, int color, boolean shadow);
    default void drawString(Font font, String text, int x, int y, int color) {
        drawString(font, text, x, y, color, false);
    }
    default void drawBorder(int x, int y, int width, int height, int color) {
        renderOutline(x, y, width, height, color);
    }
    void enableScissor(int x1, int y1, int x2, int y2);
    void disableScissor();
    void renderComponentTooltip(Font font, List<Component> lines, int mouseX, int mouseY);

    static ClickGuiCanvas of(Object graphicsOrExtractor) {
        if (graphicsOrExtractor == null) return null;
        if (graphicsOrExtractor instanceof ClickGuiCanvas c) return c;
        if (graphicsOrExtractor instanceof GuiGraphics g) {
            return new GuiGraphicsCanvas(g);
        }
        return new ExtractorCanvas(graphicsOrExtractor);
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
        private final Object extractor;

        private static boolean initialized = false;
        private static MethodHandle MH_FILL;
        private static MethodHandle MH_FILL_GRADIENT;
        private static MethodHandle MH_OUTLINE;
        private static MethodHandle MH_TEXT;
        private static MethodHandle MH_TEXT_SHADOW;
        private static MethodHandle MH_ENABLE_SCISSOR;
        private static MethodHandle MH_DISABLE_SCISSOR;
        private static MethodHandle MH_TOOLTIP;

        private static synchronized void initHandles(Class<?> clazz) {
            if (initialized) return;
            initialized = true;
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                for (Method m : clazz.getMethods()) {
                    if (m.getName().equals("fill") && m.getParameterCount() == 5 && m.getParameterTypes()[0] == int.class) {
                        MH_FILL = lookup.unreflect(m);
                    } else if (m.getName().equals("fillGradient") && m.getParameterCount() == 6) {
                        MH_FILL_GRADIENT = lookup.unreflect(m);
                    } else if (m.getName().equals("outline") && m.getParameterCount() == 5) {
                        MH_OUTLINE = lookup.unreflect(m);
                    } else if (m.getName().equals("text") && m.getParameterCount() == 5 && m.getParameterTypes()[1] == String.class) {
                        MH_TEXT = lookup.unreflect(m);
                    } else if (m.getName().equals("text") && m.getParameterCount() == 6 && m.getParameterTypes()[1] == String.class) {
                        MH_TEXT_SHADOW = lookup.unreflect(m);
                    } else if (m.getName().equals("enableScissor") && m.getParameterCount() == 4) {
                        MH_ENABLE_SCISSOR = lookup.unreflect(m);
                    } else if (m.getName().equals("disableScissor") && m.getParameterCount() == 0) {
                        MH_DISABLE_SCISSOR = lookup.unreflect(m);
                    } else if (m.getName().equals("setComponentTooltipForNextFrame") && m.getParameterCount() == 4) {
                        MH_TOOLTIP = lookup.unreflect(m);
                    }
                }
            } catch (Throwable ignored) {}
        }

        public ExtractorCanvas(Object extractor) {
            this.extractor = extractor;
            if (!initialized && extractor != null) {
                initHandles(extractor.getClass());
            }
        }

        @Override
        public void fill(int x1, int y1, int x2, int y2, int color) {
            try {
                if (MH_FILL != null) MH_FILL.invoke(extractor, x1, y1, x2, y2, color);
            } catch (Throwable ignored) {}
        }

        @Override
        public void fillGradient(int x1, int y1, int x2, int y2, int color1, int color2) {
            try {
                if (MH_FILL_GRADIENT != null) MH_FILL_GRADIENT.invoke(extractor, x1, y1, x2, y2, color1, color2);
            } catch (Throwable ignored) {}
        }

        @Override
        public void renderOutline(int x, int y, int width, int height, int color) {
            try {
                if (MH_OUTLINE != null) MH_OUTLINE.invoke(extractor, x, y, width, height, color);
            } catch (Throwable ignored) {}
        }

        @Override
        public void drawString(Font font, String text, int x, int y, int color, boolean shadow) {
            try {
                if (MH_TEXT_SHADOW != null) {
                    MH_TEXT_SHADOW.invoke(extractor, font, text, x, y, color, shadow);
                } else if (MH_TEXT != null) {
                    MH_TEXT.invoke(extractor, font, text, x, y, color);
                }
            } catch (Throwable ignored) {}
        }

        @Override
        public void enableScissor(int x1, int y1, int x2, int y2) {
            try {
                if (MH_ENABLE_SCISSOR != null) MH_ENABLE_SCISSOR.invoke(extractor, x1, y1, x2, y2);
            } catch (Throwable ignored) {}
        }

        @Override
        public void disableScissor() {
            try {
                if (MH_DISABLE_SCISSOR != null) MH_DISABLE_SCISSOR.invoke(extractor);
            } catch (Throwable ignored) {}
        }

        @Override
        public void renderComponentTooltip(Font font, List<Component> lines, int mouseX, int mouseY) {
            try {
                if (MH_TOOLTIP != null) MH_TOOLTIP.invoke(extractor, font, lines, mouseX, mouseY);
            } catch (Throwable ignored) {}
        }
    }
}
