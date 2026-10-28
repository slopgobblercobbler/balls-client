package com.clientmod.core.adapter;

/**
 * Platform adapter interface for rendering text and graphics
 * across version changes (DrawContext vs GuiGraphics).
 */
public interface IGuiRenderer<TContext, TStack> {
    void drawString(TContext context, String text, int x, int y, int color, boolean shadow);
    void drawItem(TContext context, TStack stack, int x, int y);
    void fill(TContext context, int x1, int y1, int x2, int y2, int color);
}
